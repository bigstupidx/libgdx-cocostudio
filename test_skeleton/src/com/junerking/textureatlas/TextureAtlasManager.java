package com.junerking.textureatlas;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.junerking.utils.PlistSAXHandler;

/**
 * 此管理类中有静态变量，注意在游戏退出调用clear方法
 * 
 * @author wangjunyong
 * */
public class TextureAtlasManager {
	private static final HashMap<String, TextureAtlas> texture_atlas_map = new HashMap<String, TextureAtlas>();

	public static void clear() {
		texture_atlas_map.clear();
	}

	public static TextureAtlas loadTextureAtlas(String file_name) {
		try {
			if (file_name == null || file_name.equals(""))
				return null;
			TextureAtlas result = texture_atlas_map.get(file_name);
			if (result != null)
				return result;
			if (file_name.endsWith(".plist")) {
				//cocos2d texturepacker
				long start = System.currentTimeMillis(), end;

				FileInputStream uri = new FileInputStream(Gdx.files.internal(file_name).file());
				PlistSAXHandler plistHandler = new PlistSAXHandler();
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				parser.parse(uri, plistHandler);

				end = System.currentTimeMillis();
				System.out.println("==== " + (end - start));

				result = loadFromDictinary(file_name.substring(0, file_name.lastIndexOf('/') + 1), plistHandler.getMapResult());

			} else {//libgdx texturepacker
				result = new TextureAtlas(Gdx.files.internal(file_name));
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TextureAtlas loadFromDictinary(String path, HashMap<String, Object> hash_map) {
		HashMap<String, Object> meta_data = (HashMap<String, Object>) hash_map.get("metadata");

		System.out.println("meta data: " + meta_data);
		final int format = Integer.parseInt((String) meta_data.get("format"));

		System.out.println("=== " + format);
		if (format < 0 || format > 3) {
			throw new RuntimeErrorException(null, "PList format not supported!! [format=" + format + "]");
		}

		HashMap<String, Object> frames_data = (HashMap<String, Object>) hash_map.get("frames");

		TextureAtlas atlas = new TextureAtlas();

		String file_name = path;
		if (format == 0 || format == 1 || format == 2) {
			file_name += ((String) meta_data.get("realTextureFileName"));

		} else if (format == 3) {
		}

		Texture texture = new Texture(file_name);

		Set<String> pic_names = frames_data.keySet();
		for (String name : pic_names) {
			AtlasRegion atlas_region = null;
			HashMap<String, Object> attrs = (HashMap<String, Object>) frames_data.get(name);
			System.out.println("pic_name:" + name + "" + attrs);
			if (format == 0) {
				int x = Integer.parseInt((String) attrs.get("x"));
				int y = Integer.parseInt((String) attrs.get("y"));
				int width = Integer.parseInt((String) attrs.get("width"));
				int height = Integer.parseInt((String) attrs.get("height"));
				atlas_region = new AtlasRegion(texture, x, y, width, height);
				atlas_region.offsetX = Float.parseFloat((String) attrs.get("offsetX"));
				atlas_region.offsetY = Float.parseFloat((String) attrs.get("offsetY"));
				atlas_region.originalWidth = Integer.parseInt((String) attrs.get("originalWidth"));
				atlas_region.originalHeight = Integer.parseInt((String) attrs.get("originalHeight"));

			} else if (format == 1 || format == 2) {
				String frame = (String) attrs.get("frame");
				String[] strs = getStringNormal(frame).split(",");
				int x = Integer.parseInt(strs[0]);
				int y = Integer.parseInt(strs[1]);
				int width = Integer.parseInt(strs[2]);
				int height = Integer.parseInt(strs[3]);

				atlas_region = new AtlasRegion(texture, x, y, width, height);

				String offset = (String) attrs.get("offset");
				strs = getStringNormal(offset).split(",");
				atlas_region.offsetX = Float.parseFloat(strs[0]);
				atlas_region.offsetY = Float.parseFloat(strs[1]);

				atlas_region.rotate = (Boolean) attrs.get("rotated");

				String originalWH = (String) attrs.get("sourceSize");
				strs = getStringNormal(originalWH).split(",");
				atlas_region.originalWidth = Integer.parseInt(strs[0]);
				atlas_region.originalHeight = Integer.parseInt(strs[1]);

			} else if (format == 3) {
			}
			atlas_region.name = name.substring(0, name.lastIndexOf('.'));
			System.out.println("=== " + atlas_region.name);
			atlas.addRegion(atlas_region);
		}
		return atlas;
	}

	private static String getStringNormal(String special) {
		StringBuilder result = new StringBuilder();
		for (int i = 0, n = special.length(); i < n; i++) {
			char ch = special.charAt(i);
			if (ch == '{' || ch == '}' || ch == ' ')
				continue;
			result.append(ch);
		}
		return result.toString();
	}
}
