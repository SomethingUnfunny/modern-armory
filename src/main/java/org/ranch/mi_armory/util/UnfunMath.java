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

	public static Vector2d CartesianToSpherical(Vector3d point) {
		double r = point.length();
		double theta = Math.atan2(point.x, point.z) + Math.PI / 2;
		double phi = Math.acos(point.y / r);
		return new Vector2d(theta, phi);
	}

	public static double normal(double x, double a) {
		double s = Math.sqrt(2 * Math.PI);
		double m = (1 / (a * s));
		double e = Math.pow(x / a, 2) * -0.5;
		return m * Math.pow(Math.E, e);
	}
}
