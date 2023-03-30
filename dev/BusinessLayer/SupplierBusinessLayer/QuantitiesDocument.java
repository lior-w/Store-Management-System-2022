package BusinessLayer.SupplierBusinessLayer;

import java.util.*;
import java.util.stream.Collectors;

public class QuantitiesDocument {

    public class DiscountLine {
        public final String catalogNumber;
        public final String productName;
        public final int fromAmount;
        public final double discount;

        public DiscountLine(SupplierProduct product, int fromAmount, double discount) {
            this.catalogNumber = product.getCatalogNumber();
            this.productName = product.getName();
            this.fromAmount = fromAmount;
            this.discount = discount;
        }
    }

    private List<DiscountLine> discountTable;

    public QuantitiesDocument() {
        discountTable = new ArrayList<DiscountLine>();
    }

    public List<DiscountLine> getDiscountTable() {
        return discountTable;
    }

//    public List<Discount> getDiscountsForProduct(Product product) {
//        List<Discount> discounts = new ArrayList<Discount>();
//        for (DiscountLine line : discountTable) {
//            if (line.catalogNumber.equals(product.getCatalogNumber())) {
//                discounts.add(new Discount(line.discount, line.fromAmount));
//            }
//        }
//        return discounts;
//    }

    public Map<Integer, Double> getDiscountsForProduct(SupplierProduct product) {
        Map<Integer, Double> discounts = new HashMap<Integer, Double>();
        for (DiscountLine line : discountTable) {
            if (line.catalogNumber.equals(product.getCatalogNumber())) {
                discounts.put(line.fromAmount, line.discount);
            }
        }
        return discounts;
    }

    public void addLine(SupplierProduct product, int fromAmount, double discount) {
        DiscountLine line = new DiscountLine(product, fromAmount, discount);
        if (isUnique(line)) discountTable.add(line);
    }

    private boolean isUnique(DiscountLine newLine) {
        return discountTable.stream().filter(line -> line.catalogNumber.equals(newLine.catalogNumber)).filter(line -> line.productName.equals(newLine.productName)).filter(line -> line.fromAmount == newLine.fromAmount).noneMatch(line -> line.discount == newLine.discount);
    }

    public double getDiscount(SupplierProduct product, int amount) {
        List<DiscountLine> lines = discountTable.stream().filter(line -> line.catalogNumber.equals(product.getCatalogNumber())).filter(line -> line.fromAmount <= amount).collect(Collectors.toList());
        if (!lines.isEmpty()) {
            DiscountLine line = lines.stream().reduce(lines.get(0), (line1, line2) -> line1.fromAmount > line2.fromAmount ? line1 : line2);
            return line.discount;
        } else return 0;
    }
}