package com.estar.hh.survey.view.activity.sketch.vo;

import java.io.Serializable;

public class SketchVO implements Serializable {
	private float width;//
	private float height;//
	private float x;//
	private float y;//
	private float angle;//角度
	private int path;//草图素材
	private String remark;


	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getAngle() {
		return angle;
	}
	public void setAngle(float angle) {
		this.angle = angle;
	}
	public int getPath() {
		return path;
	}
	public void setPath(int path) {
		this.path = path;
	}


}
