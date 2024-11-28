package org.example.carshowroom.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class MockResultSet {

    private final Map<String, Integer> columnIndices;
    private final Object[][] data;
    private int rowIndex;

    private MockResultSet(final String[] columnNames,
                          final Object[][] data) {
        // create a map of column name to column index
        this.columnIndices = IntStream.range(0, columnNames.length)
                .boxed()
                .collect(Collectors.toMap(
                        k -> columnNames[k],
                        Function.identity(),
                        (a, b) ->
                        {
                            throw new RuntimeException("Duplicate column " + a);
                        },
                        LinkedHashMap::new
                ));
        this.data = data;
        this.rowIndex = -1;
    }

    private ResultSet buildMock() throws SQLException {
        final var rs = mock(ResultSet.class);

        // mock rs.next()
        lenient().doAnswer(invocation -> {
            rowIndex++;
            return rowIndex < data.length;
        }).when(rs).next();

        // mock rs.getString(columnName)
        lenient().doAnswer(invocation -> {
            final var columnName = invocation.getArgument(0, String.class);
            final var columnIndex = columnIndices.get(columnName);
            return data[rowIndex][columnIndex];
        }).when(rs).getString(anyString());

        // mock rs.getInt(columnName)
        lenient().doAnswer(invocation -> {
            final var columnName = invocation.getArgument(0, String.class);
            final var columnIndex = columnIndices.get(columnName);
            return data[rowIndex][columnIndex];
        }).when(rs).getInt(anyString());

        // mock rs.getShort(columnName)
        lenient().doAnswer(invocation -> {
            final var columnName = invocation.getArgument(0, String.class);
            final var columnIndex = columnIndices.get(columnName);
            return data[rowIndex][columnIndex];
        }).when(rs).getShort(anyString());

        // mock rs.getBigDecimal(columnName)
        lenient().doAnswer(invocation -> {
            final var columnName = invocation.getArgument(0, String.class);
            final var columnIndex = columnIndices.get(columnName);
            return data[rowIndex][columnIndex];
        }).when(rs).getBigDecimal(anyString());

        return rs;
    }

    public static ResultSet create(
            final String[] columnNames,
            final Object[][] data)
            throws SQLException {
        return new MockResultSet(columnNames, data).buildMock();
    }
}
