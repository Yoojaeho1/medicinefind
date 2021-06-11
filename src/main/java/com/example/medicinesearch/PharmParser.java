package com.example.medicinesearch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class PharmParser extends Thread {

    public final static String PHARM_URL = "http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList";
    public final static String KEY = "6b%2B3xqOgMamq%2BI5CbJ8EXohcSDrrum2lhplZwGlwJqH1qqXCvPQOmiFptqaI2MAxU3DFu%2Bhl%2FbAXp5ZW3nPYmg%3D%3D";
    public static double Xpos;
    public static double Ypos;
    public static int radius = 3000;


    public PharmParser() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public ArrayList<PharmDTO> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<PharmDTO> list = new ArrayList<PharmDTO>();

        String xpos = null, ypos= null,name=null, addr=null, telno=null;
        boolean braclility_xpos = false, braclility_ypos = false, braclility_name = false, braclility_telno = false, braclility_addr = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if (tag.equals("XPos")){
                    braclility_xpos = true;
                }
                if (tag.equals("YPos")){
                    braclility_ypos = true;
                }
                if (tag.equals("yadmNm")){
                    braclility_name = true;
                }
                if (tag.equals("addr")){
                    braclility_addr = true;
                }
                if (tag.equals("telno")){
                    braclility_telno = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(braclility_xpos == true){
                    xpos = xpp.getText();
                    braclility_xpos = false;
                }else if(braclility_ypos == true){
                    ypos = xpp.getText();
                    braclility_ypos = false;
                }else if(braclility_name == true){
                    name = xpp.getText();
                    braclility_name = false;
                }else if(braclility_addr == true){
                     addr= xpp.getText();
                    braclility_addr = false;
                }else if(braclility_telno == true){
                    telno= xpp.getText();
                    braclility_telno = false;
                }

            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("item")) {
                    PharmDTO entity = new PharmDTO();
                    entity.setXpos(Double.valueOf(xpos));
                    entity.setYpos(Double.valueOf(ypos));
                    entity.setName(name);
                    entity.setAddr(addr);
                    entity.setTelno(telno);
                    list.add(entity);
                }
            }
            event_type = xpp.next();
        }

        return list;
    }




    private String getURLParam(String search){
        String url = PHARM_URL+"?ServiceKey="+KEY+"&numOfRows=200"+"&xPos="+Xpos+"&yPos="+Ypos+"&radius="+ radius;

        return url;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new PharmParser();
    }

}