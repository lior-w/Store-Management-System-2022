package ServiceLayer.SupplierServiceLayer.Obj;

import BusinessLayer.SupplierBusinessLayer.QuantitiesDocument;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceQuantitiesDocument {

    public class ServiceDiscountLine {
        public final String catalogNumber;
        public final String productName;
        public final int fromAmount;
        public final double discount;

        public ServiceDiscountLine(QuantitiesDocument.DiscountLine line) {
            this.catalogNumber = line.catalogNumber;
            this.productName = line.productName;
            this.fromAmount = line.fromAmount;
            this.discount = line.discount;
        }
    }

    public final List<ServiceDiscountLine> discountTable;

    public ServiceQuantitiesDocument(QuantitiesDocument quantitiesDocument) {
        this.discountTable = quantitiesDocument.getDiscountTable().stream().map(ServiceDiscountLine::new).collect(Collectors.toList());
    }

//    public List<ServiceDiscount> getDiscounts(String product){
//        for(ServiceProduct product1: productDiscountMap.keySet()){
//            if (product1.name== product)
//                return productDiscountMap.get(product1);
//        }
//        return null;
//    }

}
