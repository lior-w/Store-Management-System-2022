package BusinessLayer.StockBusinessLayer;

import DataAccessLayer.StockDAL.ProductUnitDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ProductUnitController {
    private ProductUnitDAO productUnitDAO;

    public ProductUnitController() {
        productUnitDAO = new ProductUnitDAO();
    }

    public boolean markProductUnitAsDefected(int unitId) {
        boolean output = productUnitDAO.update(unitId, "isDefected", 1) > 0;
        productUnitDAO.get(unitId).defected();
        return output;
    }

    public boolean addProductUnit(int productId, Date expirationDate, boolean isInStorage, int location, int discountId, int amount) throws SQLException {
        return productUnitDAO.insertAmount(productId, expirationDate, isInStorage, location, discountId, amount);
    }


    public List<ProductUnit> getDefectedProductUnits() {
        return productUnitDAO.getDefectedProductUnits();
    }

    public List<ProductUnit> getProductUnits(int id) {
        return productUnitDAO.getProductUnits(id);
    }

    public boolean removeProductUnit(int id) {
        return productUnitDAO.delete(id);
    }

    public ProductUnit getProductUnit(int id) {
        return productUnitDAO.get(id);
    }

    public boolean editProductUnit(int unitId, Date expirationDate, int location, boolean isInStorage) {
        ProductUnit unit = getProductUnit(unitId);
        productUnitDAO.update(unitId, "expirationDate", expirationDate);
        unit.setExpirationDate(expirationDate);
        productUnitDAO.update(unitId, "location", location);
        unit.setLocation(location);
        productUnitDAO.update(unitId, "inStorage", isInStorage);
        unit.setInStorage(isInStorage);
        return true;
    }
}
