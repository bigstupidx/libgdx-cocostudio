package com.junerking.ui.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.junerking.ui.UISettings;

public class UIWidgetGroup extends UIWidget {

	public UIWidgetGroup() {
		super();
	}

	public UIWidgetGroup(String name) {
		super(name);
	}

	protected ArrayList<UIWidget> child_list = new ArrayList<UIWidget>();

	public ArrayList<UIWidget> getChildrenList() {
		return child_list;
	}

	@Override
	public UIWidget findWidgetByName(String name) {
		if (this.name != null && this.name.equals(name)) {
			return this;
		}
		for (int i = 0, n = child_list.size(); i < n; i++) {
			UIWidget child = child_list.get(i);
			if (child == null)
				continue;
			UIWidget result = child.findWidgetByName(name);
			if (result != null)
				return result;
		}
		return null;
	}

	@Override
	public void prepare() {
		super.prepare();
		for (int i = 0, n = child_list.size(); i < n; i++) {
			UIWidget child = child_list.get(i);
			if (child == null)
				continue;
			child.prepare();
		}
	}

	public void clear() {
		child_list.clear();
	}

	public void setStage(Stage stage) {
		super.setStage(stage);
		for (int i = 0, n = child_list.size(); i < n; i++) {
			UIWidget child = child_list.get(i);
			if (child == null)
				continue;
			child.setStage(stage);
		}
	}

	//==========================================================================

	public void addActor(UIWidget actor) {
		if (actor == null)
			return;
		int flag = -1;
		String new_name = actor.getName();
		for (int i = 0; i < child_list.size(); i++) {
			UIWidget child = child_list.get(i);
			if (child == actor) {
				flag = 1;
				break;
			}
			if (isNameSame(child.getName(), new_name)) {
				flag = 2;
				break;
			}
		}
		if (flag == -1) {
			child_list.add(actor);
			actor.setParentWidget(this);
			return;
		}
		if (UISettings.DEBUG) {
			if (flag == 1) {
				new RuntimeException("this widget is added before!!!").printStackTrace();
			} else if (flag == 2) {
				new RuntimeException("this widget has same name with others").printStackTrace();
			}
		}
	}

	public void addActor(ArrayList<UIWidget> new_child_list, boolean clear) {
		if (clear)
			child_list.clear();
		if (new_child_list == null)
			return;
		for (int i = 0, n = new_child_list.size(); i < n; i++) {
			addActor(new_child_list.get(i));
		}
	}

	//==========================================================================

	@Override
	public void act(float delta) {
		super.act(delta);
		for (int i = 0; i < child_list.size(); i++) {
			UIWidget item = child_list.get(i);
			if (item != null) {
				item.act(delta);
			}
		}

		for (int i = 0; i < child_list.size(); i++) {
			UIWidget item = child_list.get(i);
			if (item != null && item.isMarkedRemove()) {
				item.setParentWidget(null);
				child_list.remove(i);
				--i;
			}
		}
	}

	private final Matrix3 localTransform = new Matrix3();
	private final Matrix3 worldTransform = new Matrix3();
	private final Matrix4 batchTransform = new Matrix4();
	private final Matrix4 oldBatchTransform = new Matrix4();
	private final Matrix4 tempBatchTransform = new Matrix4();
	public boolean transform = true;
	private Rectangle cullingArea;

	public void draw(SpriteBatch batch, float parentAlpha) {
		if (child_list.size() == 0)
			return;
		if (transform)
			applyTransform(batch, computeTransform());
		drawChildren(batch, parentAlpha);
		if (transform)
			resetTransform(batch);
	}

	protected void drawChildren(SpriteBatch batch, float parentAlpha) {
		parentAlpha *= getColor().a;
		Rectangle cullingArea = this.cullingArea;
		if (cullingArea != null) {
			// Draw children only if inside culling area.
			float cullLeft = cullingArea.x;
			float cullRight = cullLeft + cullingArea.width;
			float cullBottom = cullingArea.y;
			float cullTop = cullBottom + cullingArea.height;
			if (transform) {
				for (int i = 0, n = child_list.size(); i < n; i++) {
					UIWidget child = child_list.get(i);
					if (child == null || !child.isVisible())
						continue;
					float x = child.getX(), y = child.getY();
					if (x <= cullRight && y <= cullTop && x + child.getWidth() >= cullLeft
							&& y + child.getHeight() >= cullBottom)
						child.draw(batch, parentAlpha);
				}
				batch.flush();
			} else {
				// No transform for this group, offset each child.
				float offsetX = getX(), offsetY = getY();
				setX(0);
				setY(0);
				for (int i = 0, n = child_list.size(); i < n; i++) {
					UIWidget child = child_list.get(i);
					if (child == null || !child.isVisible())
						continue;
					float x = child.getX(), y = child.getY();
					if (x <= cullRight && y <= cullTop && x + child.getWidth() >= cullLeft
							&& y + child.getHeight() >= cullBottom) {
						child.setX(x + offsetX);
						child.setY(y + offsetY);
						child.draw(batch, parentAlpha);
						child.setX(x);
						child.setY(y);
					}
				}
				setX(offsetX);
				setY(offsetY);
			}
		} else {
			// No culling, draw all children.
			if (transform) {
				for (int i = 0, n = child_list.size(); i < n; i++) {
					Actor child = child_list.get(i);
					if (child == null || !child.isVisible())
						continue;
					child.draw(batch, parentAlpha);
				}
				batch.flush();
			} else {
				// No transform for this group, offset each child.
				float offsetX = getX(), offsetY = getY();
				setX(0);
				setY(0);
				for (int i = 0, n = child_list.size(); i < n; i++) {
					Actor child = child_list.get(i);
					if (child == null || !child.isVisible())
						continue;
					float x = child.getX(), y = child.getY();
					child.setX(x + offsetX);
					child.setY(y + offsetY);
					child.draw(batch, parentAlpha);
					child.setX(x);
					child.setY(y);
				}
				setX(offsetX);
				setY(offsetY);
			}
		}
	}

	protected void applyTransform(SpriteBatch batch, Matrix4 transform) {
		oldBatchTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(tempBatchTransform.set(oldBatchTransform).mul(transform));
		System.out.println(name + "\n" + batch.getTransformMatrix());
	}

	protected Matrix4 computeTransform() {
		float originX = getOriginX();
		float originY = getOriginY();
		float rotation = getRotation();
		float scaleX = getScaleX();
		float scaleY = getScaleY();

		if (originX != 0 || originY != 0)
			localTransform.setToTranslation(originX, originY);
		else
			localTransform.idt();
		if (rotation != 0)
			localTransform.rotate(rotation);
		if (scaleX != 1 || scaleY != 1)
			localTransform.scale(scaleX, scaleY);
		if (originX != 0 || originY != 0)
			localTransform.translate(-originX, -originY);
		localTransform.trn(getX(), getY());

		// Find the first parent that transforms.
		Group parentGroup = getParent();
		while (parentGroup != null) {
			if (parentGroup.transform)
				break;
			parentGroup = parentGroup.getParent();
		}

		if (parentGroup != null) {
			worldTransform.set(parentGroup.worldTransform);
			worldTransform.mul(localTransform);
		} else {
			worldTransform.set(localTransform);
		}
		
		System.out.println("== " + worldTransform);

		batchTransform.set(worldTransform);
		return batchTransform;
	}

	protected void resetTransform(SpriteBatch batch) {
		batch.setTransformMatrix(oldBatchTransform);
	}

	public void setCullingArea(Rectangle cullingArea) {
		this.cullingArea = cullingArea;
	}

	//==========================================================================

	public void sortChildren(Comparator<Actor> comparator) {
		Collections.sort(child_list, comparator);
	}

	//==========================================================================
	protected final Vector2 point = new Vector2();

	//touch事件的分发
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		for (int i = child_list.size() - 1; i >= 0; i--) {
			UIWidget child = child_list.get(i);
			if (child == null || !child.isVisible() || child.touchable == Touchable.disabled)
				continue;
			child.parentToLocalCoordinates(point.set(x, y));
			Actor hit = child.hit(point.x, point.y, touchable);
			if (hit != null) {
				return hit;
			}
		}
		return super.hit(x, y, touchable);
	}
}
