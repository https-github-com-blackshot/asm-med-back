package kz.beeset.med.device.model.graph.components;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Options {

    private boolean spanGaps;
    private Legend legend;
    private boolean maintainAspectRatio;
    private Tooltip tooltips;
    private Layout layout;
    private Element elements;
    private Scale scales;
    private Plugin plugins;

    public static Options getDefauleOptions() {
        Options options = new Options();
        options.setSpanGaps(false);

        Legend legend = new Legend();
        legend.setDisplay(true);
        options.setLegend(legend);

        options.setMaintainAspectRatio(false);

        Tooltip tooltip = new Tooltip();
        tooltip.setPosition("nearest");
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltips(tooltip);

        Layout layout = new Layout();
        Layout.Padding padding = new Layout.Padding();
        padding.setLeft(24);
        padding.setRight(32);
        layout.setPadding(padding);
        options.setLayout(layout);

        Element element = new Element();
        Element.Point point = new Element.Point();
        point.setRadius(4);
        point.setBorderWidth(2);
        point.setHoverRadius(4);
        point.setHoverBorderWidth(2);
        element.setPoint(point);
        options.setElements(element);

        Scale scale = new Scale();

        List<Scale.xAxes> xAxes = new ArrayList<>();

        Scale.xAxes axes1 = new Scale.xAxes();
        Scale.xAxes.GridLine gridLine1 = new Scale.xAxes.GridLine();
        gridLine1.setDisplay(false);
        Scale.xAxes.Tick tick1 = new Scale.xAxes.Tick();
        tick1.setFontColor("rgba(0,0,0,0.54)");
        axes1.setGridLines(gridLine1);
        axes1.setTicks(tick1);

        xAxes.add(axes1);

        List<Scale.yAxes> yAxes = new ArrayList<>();

        Scale.yAxes axesA = new Scale.yAxes();
        Scale.yAxes axesB = new Scale.yAxes();
        Scale.yAxes.GridLine gridLine2 = new Scale.yAxes.GridLine();
        gridLine2.setTickMarkLength(16);
        Scale.yAxes.yTick tickA = new Scale.yAxes.yTick();
        Scale.yAxes.yTick tickB = new Scale.yAxes.yTick();
        tickA.setStepSize(10);
        tickA.setFontColor("rgba(0,0,0,0.54)");
        tickA.setMin(0);
        tickA.setMax(140);
        tickB.setStepSize(1000);
        tickB.setFontColor("rgba(0,0,0,0.54)");
        tickB.setMin(0);
        tickB.setMax(10000);
//        axes2.setGridLines(gridLine2);
//        axes2.setTicks(tick2);
        axesA.setId("A");
        axesA.setType("linear");
        axesA.setPosition("left");
        axesA.setTicks(tickA);

        axesB.setId("B");
        axesB.setType("linear");
        axesB.setPosition("left");
        axesB.setTicks(tickB);

        yAxes.add(axesA);
        yAxes.add(axesB);

        scale.setXAxes(xAxes);
        scale.setYAxes(yAxes);
        options.setScales(scale);

        Plugin plugin = new Plugin();
        Plugin.Filler filler = new Plugin.Filler();
        filler.setPropagate(false);
        plugin.setFiller(filler);
        options.setPlugins(plugin);

        return options;
    }


    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Legend {
        private boolean display;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Tooltip {
        private String position;
        private String mode;
        private boolean intersect;
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Layout {

        private Padding padding;

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class Padding {
            private Integer left;
            private Integer right;
        }

    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Element {

        private Point point;

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class Point {
            private Integer radius;
            private Integer borderWidth;
            private Integer hoverRadius;
            private Integer hoverBorderWidth;
        }

    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Scale {

        private List<xAxes> xAxes;
        private List<yAxes> yAxes;

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class xAxes {
            private GridLine gridLines;
            private Tick ticks;

            @Getter
            @Setter
            @EqualsAndHashCode
            public static class GridLine {
                private Boolean display;
                private Integer tickMarkLength;
            }

            @Getter
            @Setter
            @EqualsAndHashCode
            public static class Tick {
                private String fontColor;
                private Integer stepSize;
            }

        }

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class yAxes {
//            private GridLine gridLines;
            private yTick ticks;
            private String id;
            private String type;
            private String position;

            @Getter
            @Setter
            @EqualsAndHashCode
            public static class GridLine {
                private Boolean display;
                private Integer tickMarkLength;
            }

            @Getter
            @Setter
            @EqualsAndHashCode
            public static class yTick {
                private String fontColor;
                private Integer stepSize;
                private Integer max;
                private Integer min;
            }

        }

    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Plugin {

        private Filler filler;

        @Getter
        @Setter
        @EqualsAndHashCode
        public static class Filler {
            private boolean propagate;
        }

    }



}
