package com.urbanek.routingproblem.utils.drawer;

import com.urbanek.routingproblem.employes.dtos.Employee;
import com.urbanek.routingproblem.employes.services.EmployeeService;
import com.urbanek.routingproblem.ga.config.Configs;
import com.urbanek.routingproblem.ga.randomkey.LocationRandomKey;
import com.urbanek.routingproblem.ga.statistics.StatisticsAggregator;
import com.urbanek.routingproblem.geo.locations.services.LocationService;
import lombok.Builder;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Ta klasa to mi chat gpt wygenerowal i ogolnie jest syfiasta, ale cos drukuje.
 * Domyslnie to i tak do wywalenia, bo bede chcial pokazywac rezultaty na mapie,
 * ale to taki fajny bajerek, zeby cos wyswietlic
 */
public class GraphDrawer extends JFrame {
    private static final int MARGIN_TOP = 50;
    private static final int MARGIN_LEFT = 110;
    private static final int POINT_MULTIPLIER = 10;

    private final LocationService locationService;
    private final StatisticsAggregator statisticsAggregator;

    private Map<Color, List<LocationPoint>> pointGroups;
    private final Map<String, Color> employeeIdToColor;


    public GraphDrawer(LocationService locationService, StatisticsAggregator statisticsAggregator,
                       EmployeeService employeeService, int locationAmount) {
        this.locationService = locationService;
        this.statisticsAggregator = statisticsAggregator;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(locationAmount * 1000, locationAmount * 1000);

        employeeIdToColor = employeeService.getAllEmployees()
                .stream()
                .collect(Collectors.toMap(Employee::getId, employee -> RandomColorPicker.getRandomColor()));
        pointGroups = new HashMap<>();
    }


    public void addPoints(int generation) {
        setTitle("Routes - Generation " + generation);

        int screenWidth = getWidth();
        int screenHeight = getHeight();
        double screenProportion = (double) screenWidth / screenHeight;

        List<LocationRandomKey> bestLocationNumberSeries = statisticsAggregator.getSeriesWithBestResult()
                .getBestResult()
                .locationRandomKeys();
        pointGroups = locationService.getOrderedLocationGroupByEmployee(bestLocationNumberSeries)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> employeeIdToColor.get(entry.getKey()), entry -> entry.getValue()
                        .stream()
                        .map(value -> LocationPoint.builder()
                                .point(new Point2D.Double(value.x() * POINT_MULTIPLIER * screenProportion + MARGIN_LEFT,
                                        value.y() * POINT_MULTIPLIER + MARGIN_TOP))
                                .label(value == Configs.DEPOT ? "Warszawa" : null)
                                .build())
                        .collect(Collectors.toList())));
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the legend
        int legendY = MARGIN_TOP;
        for (String label : employeeIdToColor.keySet()) {
            Color color = employeeIdToColor.get(label);
            g.setColor(color);
            g.fillRect(WIDTH + 10, legendY - 10, 20, 20);
            g.setColor(Color.BLACK);
            g.drawString(label, WIDTH + 35, legendY);
            legendY += 30;
        }

        for (Color color : pointGroups.keySet()) {
            g.setColor(color);
            for (LocationPoint locationPoint : pointGroups.get(color)) {
                Point2D.Double point = locationPoint.point;
                int x = (int) Math.round(point.x);
                int y = (int) Math.round(point.y);
                g.fillOval(x, y, 10, 10);
                if (Objects.nonNull(locationPoint.label)) {
                    g.setColor(Color.BLACK);
                    g.drawString(locationPoint.label, x, y);
                    g.setColor(color);
                }

            }
        }

        for (Color color : pointGroups.keySet()) {
            g.setColor(color);
            List<Point2D.Double> pointsForColor = pointGroups.get(color)
                    .stream()
                    .map(locationPoint -> locationPoint.point)
                    .toList();
            if (pointsForColor.size() > 1) {
                for (int i = 1; i < pointsForColor.size(); i++) {
                    Point2D.Double prev = pointsForColor.get(i - 1);
                    Point2D.Double curr = pointsForColor.get(i);
                    g.drawLine((int) Math.round(prev.x), (int) Math.round(prev.y),
                            (int) Math.round(curr.x), (int) Math.round(curr.y));
                }
            }
        }
    }

    @Builder
    private record LocationPoint(String label, Point2D.Double point) {
    }
}
