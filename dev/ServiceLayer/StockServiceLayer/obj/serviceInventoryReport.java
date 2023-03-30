package ServiceLayer.StockServiceLayer.obj;

import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.StockServiceLayer.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class serviceInventoryReport {

    public String showReport(StockService stockService) {
        String output = "\nInventory Report - " + new SimpleDateFormat("yyyy-MM-dd hh:mm:s").format(Date.from(Instant.now())) + "\n\n";
        ResponseT<List<serviceCategory>> response = stockService.getTopLevelCategories();
        if (response.isError()) return response.getError();
        List<serviceCategory> categories = response.getData();
        if (categories != null) {
            output += "Categories: [";
            for (serviceCategory c : categories) {
                output += "\n\t{\n\t\tName:" + c.getCategoryName() + " \n\t\tID: " + c.getCategoryId() + " \n\t\tSub-Categories: [";
                ResponseT<List<serviceCategory>> subResponse = stockService.getSubCategories(c.getCategoryId());
                if (subResponse.isError()) return subResponse.getError();
                List<serviceCategory> subCategories = subResponse.getData();
                if (subCategories != null) {
                    for (serviceCategory subC : subCategories) {
                        output += "\n\t\t{\n\t\t\tName: " + subC.getCategoryName() + " \n\t\t\tID: " + subC.getCategoryId() + "\n\t\t\tSub-Categories: [";
                        ResponseT<List<serviceCategory>> subSubResponse = stockService.getSubCategories(subC.getCategoryId());
                        if (subSubResponse.isError()) return subSubResponse.getError();
                        List<serviceCategory> subSubCategories = subSubResponse.getData();
                        if (subSubCategories != null) {
                            for (serviceCategory subSubC : subSubCategories) {
                                output += "\n\t\t\t{\n\t\t\t\tName: " + subSubC.getCategoryName() +
                                          "\n\t\t\t\tID: " + subSubC.getCategoryId() +
                                          "\n\t\t\t\tProducts: [";
                                ResponseT<List<serviceProduct>> productResponse = stockService.getProducts(subSubC.getCategoryId());
                                if (productResponse.isError()) return subResponse.getError();
                                List<serviceProduct> products = productResponse.getData();
                                if (products != null) {
                                    for (serviceProduct product : products) {
                                        String saleDiscount;
                                        if (product.getSaleDiscount() != -1) {
                                            ResponseT<serviceSaleDiscount> saleDiscountResponse = stockService.getSaleDiscount(product.getSaleDiscount());
                                            if (saleDiscountResponse.isError()) saleDiscount = "Error Getting Discount";
                                            else saleDiscount = saleDiscountResponse.getData().getDiscount() + "%";
                                        }
                                        else saleDiscount = "0%";
                                        output += "\n\t\t\t\t{\n\t\t\t\t\tName: " + product.getName() + " \n\t\t\t\t\tID: " + product.getId() +
                                                "\n\t\t\t\t\tAmount/Min Quantity: " + product.getAmount() + "/" + product.getMinQuantity() +
                                                "\n\t\t\t\t\tProducer: " + product.getProducer() +
                                                "\n\t\t\t\t\tSale Price: " + product.getSalePrice() +
                                                "\n\t\t\t\t\tSale Discount: " + saleDiscount +
                                                "\n\t\t\t\t\tProduct Units: [";
                                        ResponseT<List<serviceProductUnit>> unitResponse = stockService.getProductUnits(product.getId());
                                        if (unitResponse.isError()) return unitResponse.getError();
                                        List<serviceProductUnit> units = unitResponse.getData();
                                        if (units != null) {
                                            for (serviceProductUnit unit : units) {
                                                String discount;
                                                if (unit.getSupplierDiscountId() != -1) {
                                                    ResponseT<serviceSupplierDiscount> discountResponse = stockService.getSupplierDiscount(unit.getSupplierDiscountId());
                                                    if (discountResponse.isError()) discount = "Error Getting Discount";
                                                    else discount = discountResponse.getData().toString() + "%";
                                                }
                                                else discount = "0%";
                                                output += "\n\t\t\t\t\t\t{ " +
                                                        "\n\t\t\t\t\t\t\tID: " + unit.getId() +
                                                        "\n\t\t\t\t\t\t\tExpiration Date: " + new SimpleDateFormat("yyyy-MM-dd").format(unit.getExpirationDate()) +
                                                        "\n\t\t\t\t\t\t\tLocation: " + unit.getLocation() +
                                                        "\n\t\t\t\t\t\t\tIn Storage? " + unit.isInStorage() +
                                                        "\n\t\t\t\t\t\t\tIs Defected/Expired? " + unit.isDefected() +
                                                        "\n\t\t\t\t\t\t\tSupplier Discount: " + discount +
                                                        "\n\t\t\t\t\t\t}";
                                            }
                                            output += "\n\t\t\t\t\t]";
                                        }
                                        output += "\n\t\t\t\t\t}";
                                    }
                                    output += "\n\t\t\t\t]";
                                }
                                output += "\n\t\t\t}";
                            }
                            output += "\n\t\t]";
                        }
                        output += "\n\t\t}";
                    }
                    output += "\n\t]";
                }
                output += "\n\t}";
            }
            output += "\n]";
        }
        return output;
    }
}
