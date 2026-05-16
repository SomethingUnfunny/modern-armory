package org.ranch.mi_armory.util;

import org.joml.Vector2d;

import java.util.Iterator;

public class GSPIterator implements Iterator<Vector2d> {

	private int i;
	private final int n;

	public GSPIterator(int n) {
		this.n = n;
		this.i = 0;
	}

	@Override
	public boolean hasNext() {
		return i < n;
	}

	@Override
	public Vector2d next() {
		double theta = Math.PI * (1 + Math.sqrt(5)) * i;
		double phi = Math.acos(1 - (double) (2 * i) / n);
		i++;
		return new Vector2d(theta, phi);
	}
}