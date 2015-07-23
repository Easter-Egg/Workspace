package utils;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

public class NullConnectionRouter extends AbstractRouter {

	public void route(Connection conn) {
		PointList points = conn.getPoints();
		points.removeAllPoints();
		Point p;
		p = getStartPoint(conn);
		conn.translateToRelative(p = getStartPoint(conn));
		points.addPoint(p);
		p = getEndPoint(conn);
		conn.translateToRelative(p = getEndPoint(conn));
		points.addPoint(p);
		conn.setPoints(points);
	}
}
