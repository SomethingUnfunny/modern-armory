package org.ranch.mi_armory.util;

import org.joml.Vector2d;
import org.joml.Vector3d;

public class UnfunMath {
	public static Vector3d sphericalToCartesian(Vector2d point) {
		double x = Math.cos(point.x) * Math.sin(point.y);
		double z = Math.sin(point.x) * Math.sin(point.y);
		double y = Math.cos(point.y);
		return new Vector3d(x, y, z);
	}
}
