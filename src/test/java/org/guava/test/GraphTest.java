package org.guava.test;

import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.Test;

public class GraphTest {

    @Test
    public void valueGraphTest() {
        ValueGraph<String, Integer> valueGraph = ValueGraphBuilder.directed().build();

    }

}
