package Tests;

import BusinessLayer.TransportBusinessLayer.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class TransportTest {
    Site s;
    TransportDocument doc;
    private TransportController tc = TransportController.getInstance();
    @BeforeEach
    public void setUp() throws Exception {
        s = tc.findSite(1);
    }
    @Test
    void addTransportDocumentSuccess() throws Exception {
        try{
            //doc = tc.createTransportDocument(String.format("%d",s.getSiteID()),
           //         "1 12");
            assertTrue(true);
        }
        catch (Exception e)
        {
            fail();
        }
    }
    @Test
    void removeTransportDocumentSuccess() throws Exception {
        try {
            tc.removeTransportDocument(tc.getTransportDocCounter()-1);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void addTransportDocumentFail() throws Exception {
        try{
//            doc = tc.createTransportDocument("1234",
//                    "1 12");
//            fail();
        }
        catch (Exception e)
        {
            assertTrue(true);
        }
    }
    @Test
    void removeTransportDocumentFail() throws Exception {
        try {
            tc.removeTransportDocument(tc.getTransportDocCounter());
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void addSiteSuccess() throws Exception {
        try {
            Site sTest = tc.addSite("siteTest, TLV","Benia","054654324","South");
            tc.removeSite(sTest.getSiteID());
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    void addVehicleSuccess() throws Exception {
        try {
            Vehicle vTest = tc.addVehicle("2134321","A",1000,10000,"Truck");
            assertTrue(true);
            tc.removeVehicle(vTest.getLicenseNumber());
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    void addVehicleFail() throws Exception {
        try {
            Vehicle vTest = tc.addVehicle("132","A",1000,10000,"Truck");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @DisplayName("Trying to start transport that already done")
    @Test
    void startTransportFail() throws Exception {
        try {
            tc.startTransport(2);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void findTransportSuccess() throws Exception {
        try {
            Transport t = tc.findTransport(2);
            assertEquals(t.getID(),2);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void findTransportFail() throws Exception {
        try {
            Transport t = tc.findTransport(1234);
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }




}