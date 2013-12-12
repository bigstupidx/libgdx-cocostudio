package com.junerking.skeleton.sdp;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class XAnimation extends IAnimation {

	private HashMap<Integer, AtlasRegion> atlas_region = new HashMap<Integer, AtlasRegion>();

	protected int _action_count;

	protected String[] _action_ids;
	protected String[] _nextaction_ids;

	protected XAnimationFrames[] _actions;
	protected TextureAtlas _texture_atlas;

	public XAnimation(int frame_count) {
		_action_count = frame_count;
		_action_ids = new String[_action_count];
		_nextaction_ids = new String[_action_count];
		_actions = new XAnimationFrames[_action_count];
	}

	public void addAtlasComponent(int id, AtlasRegion region) {
		atlas_region.put(id, region);
	}

	//===================================================================================
	public final class State {
		int action_index = -1, frame_index, speed = 150, size = 0, last_frame_index;
		long time = 0;
		boolean flip_x;

		public void tick(float time_span) {
			if (action_index >= 0) {
				time += time_span;
				int count = (int) (time / speed);
				last_frame_index = frame_index;
				frame_index = count % size;
				if (count % size == 0 && last_frame_index != frame_index) {
					boolean action_changed = false;
					if (!action_changed) {
						changeAction(_nextaction_ids[action_index]);
					}
				}
			}
		}

		public void changeAction(String animation_name) {
			if (animation_name == null || animation_name.equals("")) {
				_changeAction(-1);
				return;
			}
			for (int i = _action_ids.length - 1; i >= 0; i--) {

				if (animation_name.equals(_action_ids[i])) {
					_changeAction(i);
					return;
				}
			}
			_changeAction(-1);
		}

		protected void _changeAction(int nextaction) {
			System.out.println("==== " + nextaction);
			action_index = nextaction;
			frame_index = 0;
			time = 0;
			if (action_index >= 0) {
				size = _actions[action_index].size;
			}
		}

		public boolean isFlipX() {
			return flip_x;
		}

		public int getFrameId() {
			return frame_index;
		}

		public int getActionIndex() {
			return action_index;
		}

		public String getAction() {
			if (action_index == -1)
				return null;
			return _action_ids[action_index];
		}

		public IAnimation getAnimation() {
			return XAnimation.this;
		}
	}

	public State createState() {
		return new State();
	}

	//===================================================================================
	//---------------------render------------------------------

	private Matrix4 mutable_matrix = new Matrix4();
	private Matrix4 temp_matrix = new Matrix4();

	public void render(SpriteBatch sprite_batch, float x, float y, float sx, float sy, int action_index, int frame_index, boolean flip_x) {
		if (action_index >= _action_count || action_index < 0)
			return;

		final XAnimationFrames ca = _actions[action_index];

		if (frame_index < 0 || frame_index >= ca.size)
			return;

		final XSingleFrame frame = ca.frames_array.get(frame_index);
		if (frame == null)
			return;

		temp_matrix.set(sprite_batch.getTransformMatrix().val);
		mutable_matrix.set(temp_matrix);
		mutable_matrix.translate(x, y, 0);

		if (flip_x) {
			mutable_matrix.scale(-sx, sy, 0);
		} else {
			mutable_matrix.scale(sx, sy, 0);
		}

		sprite_batch.setTransformMatrix(mutable_matrix);

		final ArrayList<XPosition> positions_array = frame.getPositionArray();
		for (XPosition p : positions_array) {
			final AtlasRegion r = atlas_region.get(p.type);
			if (r != null)
				sprite_batch.draw(r, p.x1, p.y1, p.x2, p.y2, p.x3, p.y3, p.x4, p.y4, r.getU(), r.getV(), r.getU2(), r.getV2());
		}

		sprite_batch.setTransformMatrix(temp_matrix);
	}

	//===================================================================================
	//----------------------parse-----------------------

	public static XAnimation createAnimation(String file_name) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			System.out.print("" + Gdx.files.internal(""));
			Document doc = db.parse(Gdx.files.internal(file_name).file());
			doc.normalize();
			return getCharData(doc, file_name.substring(0, file_name.lastIndexOf('/') + 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XAnimation getCharData(Document doc, String file_name_pre) throws ParserConfigurationException, SAXException {
		XAnimation data = null;

		NodeList headers = doc.getElementsByTagName("XAnimation");
		data = new XAnimation(getIntValue((Element) (headers.item(0)), "frame_count", 1));

		HashMap<Integer, Vector2> component_list = new HashMap<Integer, Vector2>();
		//components
		{
			NodeList _components = doc.getElementsByTagName("components");
			Element components = (Element) _components.item(0);
			String texture_atlas_name = components.getAttribute("texture_atlas");
			TextureAtlas texture_atlas = new TextureAtlas(file_name_pre + texture_atlas_name);
			NodeList nodes = components.getElementsByTagName("component");
			for (int i = 0, n = nodes.getLength(); i < n; i++) {
				Element element = (Element) (nodes.item(i));
				int component_id = getIntValue(element, "type", -1);
				int w = getIntValue(element, "w", 0);
				int h = getIntValue(element, "h", 0);
				Vector2 vc = new Vector2(w, h);
				component_list.put(component_id, vc);
				String component_name = element.getAttribute("name");
				AtlasRegion region = texture_atlas.findRegion(component_name);
				if (region != null) {
					data.addAtlasComponent(component_id, region);
				}
			}
		}

		{
			NodeList _frames = doc.getElementsByTagName("Frames");
			for (int ii = 0, n = _frames.getLength(); ii < n; ii++) {
				Element frames = (Element) (_frames.item(ii));
				boolean loop = getBooleanValue(frames, "loop", true);
				data._action_ids[ii] = getStringValue(frames, "id");
				if (!loop) {
					data._nextaction_ids[ii] = getStringValue(frames, "next");
				} else {
					data._nextaction_ids[ii] = null;
				}
				XAnimationFrames animation_frames = new XAnimationFrames();

				NodeList frame_data = frames.getElementsByTagName("frame");
				for (int jj = 0, m = frame_data.getLength(); jj < m; jj++) {
					Element _single_frame = (Element) (frame_data.item(jj));
					XSingleFrame single_frame = new XSingleFrame();

					NodeList pos_list = _single_frame.getElementsByTagName("position");
					for (int kk = 0, len = pos_list.getLength(); kk < len; kk++) {
						Element _pos = (Element) (pos_list.item(kk));
						int region_id = getIntValue(_pos, "type", 0);
						float tx = getFloatValue(_pos, "x", 0);
						float ty = getFloatValue(_pos, "y", 0);
						float sx = getFloatValue(_pos, "sx", 0);
						float sy = getFloatValue(_pos, "sy", 0);
						float rx = getFloatValue(_pos, "rx", 0);
						float ry = getFloatValue(_pos, "ry", 0);

						Vector2 com = component_list.get(region_id);

						AtlasRegion region = data.atlas_region.get(region_id);
						if (region == null || region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
							XXXPosition(com.x, com.y, null, tx, ty, sx, sy, rx, ry, 1.0f);
						} else {
							XXXPosition(com.x, com.y, region, tx, ty, sx, sy, rx, ry, 1.0f);
						}
						XPosition p = new XPosition(region_id, x1, y1, x2, y2, x3, y3, x4, y4);
						single_frame.addPosition(p);
					}
					animation_frames.addFrame(single_frame);
				}
				data._actions[ii] = animation_frames;
			}
		}
		component_list.clear();
		component_list = null;
		return data;
	}

	public static int getIntValue(Element element, String key, int defaultValue) {
		if (element.hasAttribute(key)) {
			return Integer.parseInt(element.getAttribute(key));
		}
		return defaultValue;
	}

	public static float getFloatValue(Element element, String key, float defaultValue) {
		if (element.hasAttribute(key)) {
			return Float.parseFloat(element.getAttribute(key));
		}
		return defaultValue;
	}

	public static boolean getBooleanValue(Element element, String key, boolean defaultValue) {
		if (element.hasAttribute(key)) {
			return Boolean.parseBoolean(element.getAttribute(key));
		}
		return defaultValue;
	}

	public static String getStringValue(Element element, String key) {
		return element.getAttribute(key);
	}

	//========================================================================
	//------------------------static classes----------------------------------
	//========================================================================

	public static class XSingleFrame {
		private ArrayList<XPosition> positions_array;

		public XSingleFrame() {
			positions_array = new ArrayList<XPosition>();
		}

		public void addPosition(XPosition position) {
			positions_array.add(position);
		}

		public ArrayList<XPosition> getPositionArray() {
			return positions_array;
		}
	}

	private static class XPosition {
		public int type;
		public float x1, x2, x3, x4;
		public float y1, y2, y3, y4;

		public XPosition(int type, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
			this.type = type;

			this.x1 = x1;
			this.y1 = y1;

			this.x2 = x2;
			this.y2 = y2;

			this.x3 = x3;
			this.y3 = y3;

			this.x4 = x4;
			this.y4 = y4;
		}
	}

	private static float x1, y1, x2, y2, x3, y3, x4, y4;

	public static void XXXPosition(float originalWidth, float originalHeight, AtlasRegion region, float x, float y, float sx, float sy,
			float rs0, float rs1, float scale) {

		if (region != null) {
			float scalew = originalWidth * 1.0f / region.originalWidth;
			float scaleh = originalHeight * 1.0f / region.originalHeight;

			float ox = region.offsetX * scalew;
			float oy = (region.originalHeight - region.packedHeight - region.offsetY) * scaleh;
			float pw = region.packedWidth * scalew;
			float ph = region.packedHeight * scaleh;

			x1 = sx * ox + rs1 * (-oy) + x;
			y1 = rs0 * ox + sy * (-oy) + y;

			x2 = sx * (ox + pw) + rs1 * (-oy) + x;
			y2 = rs0 * (ox + pw) + sy * (-oy) + y;

			x3 = sx * (ox + pw) + rs1 * (-oy - ph) + x;
			y3 = rs0 * (ox + pw) + sy * (-oy - ph) + y;

			x4 = sx * ox + rs1 * (-oy - ph) + x;
			y4 = rs0 * ox + sy * (-oy - ph) + y;
		} else {
			x1 = x;
			y1 = y;

			x2 = sx * originalWidth + x;
			y2 = rs0 * originalWidth + y;

			x3 = sx * originalWidth + rs1 * (-originalHeight) + x;
			y3 = rs0 * originalWidth + sy * (-originalHeight) + y;

			x4 = rs1 * (-originalHeight) + x;
			y4 = sy * (-originalHeight) + y;

		}
		x1 *= scale;
		y1 *= scale;
		x2 *= scale;
		y2 *= scale;
		x3 *= scale;
		y3 *= scale;
		x4 *= scale;
		y4 *= scale;
	}

	public static class XAnimationFrames {
		private ArrayList<XSingleFrame> frames_array = new ArrayList<XSingleFrame>();
		private int size;

		public void addFrame(XSingleFrame singleFrame) {
			frames_array.add(singleFrame);
			++size;
		}
	}
}
