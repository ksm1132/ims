package com.ims.inventorymgmtsys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventorymgmtsys.repository.SalesRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SalesServiceImpl implements SalesService {
    private final SalesRepository salesRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SalesServiceImpl(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<Map<String, Object>> getSalesData() {
//        return salesRepository.getSalesData();
        List<Map<String, Object>> salesData = salesRepository.getSalesData();
        salesData.forEach(System.out::println);
        return salesData;
    }

    @Override
    public String getSalesDataAsJson() {
        try {
            return objectMapper.writeValueAsString(getSalesData());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert sales data to Json");
        }
    }



    public void generateSalesChart(HttpServletResponse httpServletResponse) throws IOException {
        List<Map<String, Object>> salesData = getSalesData();
        if (salesData == null || salesData.isEmpty()) {
            httpServletResponse.sendError(HttpServletResponse.SC_NO_CONTENT, "No sales data available");
            return;
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map<String, Object> sale : salesData) {
            String orderId = (String) sale.get("order_id");
            Number total = (Number) sale.get("total_yen");
            if (total != null) {
                dataset.addValue(total, "注文金額", orderId);
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "注文別",
                "注文ID",
                "金額（円）",
                dataset
        );

        Font japaneseFont = new Font("MS Gothic", Font.PLAIN, 24);
        barChart.setTitle(new TextTitle(barChart.getTitle().getText(), japaneseFont));

        CategoryPlot plot = (CategoryPlot) barChart.getPlot();

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelFont(japaneseFont);

        LegendTitle legend = barChart.getLegend();
        legend.setItemFont(japaneseFont);

        plot.getDomainAxis().setLabelFont(japaneseFont);
        plot.getDomainAxis().setTickLabelFont(japaneseFont);

        plot.getRangeAxis().setLabelFont(japaneseFont);
        plot.getRangeAxis().setTickLabelFont(japaneseFont);

        barChart.setBackgroundPaint(Color.white);
        httpServletResponse.setContentType("image/png");
        ChartUtils.writeChartAsPNG(httpServletResponse.getOutputStream(), barChart, 1000, 800);
    }

    @Override
    public List<Map<String, Object>> getDailySalesData() {
        List<Map<String, Object>> salesData = salesRepository.getDailySalesData();
        salesData.forEach(System.out::println);
        return salesData;
    }

    @Override
    public String getDailySalesDataAsJson () {
        try {
            return objectMapper.writeValueAsString(getDailySalesData());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert daily sales data to JSON");
        }
    }

    @Override
    public void generateDailySalesChart(HttpServletResponse httpServletResponse) throws IOException {
        List<Map<String, Object>> salesData = getDailySalesData();
        if (salesData == null || salesData.isEmpty()) {
            httpServletResponse.sendError(HttpServletResponse.SC_NO_CONTENT, "No sales data available");
            return;
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map<String, Object> sale : salesData) {
            String date = (String) sale.get("date");
            Number total = (Number) sale.get("total_date_yen");
            if (total != null) {
                dataset.addValue(total, "累積金額", date);
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "日次",
                "日付",
                "金額（円）",
                dataset
        );

        Font japaneseFont = new Font("MS Gothic", Font.PLAIN, 24);
        barChart.setTitle(new TextTitle(barChart.getTitle().getText(), japaneseFont));

        CategoryPlot plot = (CategoryPlot) barChart.getPlot();

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelFont(japaneseFont);

        LegendTitle legend = barChart.getLegend();
        legend.setItemFont(japaneseFont);

        plot.getDomainAxis().setLabelFont(japaneseFont);
        plot.getDomainAxis().setTickLabelFont(japaneseFont);

        plot.getRangeAxis().setLabelFont(japaneseFont);
        plot.getRangeAxis().setTickLabelFont(japaneseFont);

        barChart.setBackgroundPaint(Color.white);
        httpServletResponse.setContentType("image/png");
        ChartUtils.writeChartAsPNG(httpServletResponse.getOutputStream(), barChart, 1000, 800);
    }

}
