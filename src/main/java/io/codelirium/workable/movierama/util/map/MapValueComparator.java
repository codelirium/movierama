package io.codelirium.workable.movierama.util.map;

import java.util.Comparator;
import java.util.Map;


public class MapValueComparator implements Comparator<Long> {

	private Map<Long, Double> map;


	public MapValueComparator(final Map<Long, Double> map) {

		this.map = map;

	}


	@Override
	public int compare(final Long a, final Long b) {

		if (map.get(a) >= map.get(b)) {

			return -1;

		} else {

			return 1;

		}
	}
}
