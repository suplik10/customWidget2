package test.ctrl;

import java.util.Timer;
import java.util.TimerTask;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.gmaps.Gpolygon;
import org.zkoss.gmaps.event.MapMouseEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import eu.safetyfirst.gmapsextension.SFMapControl;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class SFTestController extends  SelectorComposer<Component>{

	@Wire
    Gmaps gmaps;

	Gmarker gm;
	Gmarker gm2;
	Gmarker gm3;
	Gmarker gm4;
	
	
	SFMapControl mapControl;

	@Wire
	Window organizationUnitEditDialog;
	
	@Wire
	Button streetButt;
	@Wire
	Button addMyGmarker;
	@Wire
	Button stopBound;
	@Wire
	Button poiButt;
	@Wire
	Button crossButt;
	@Wire
	Button kmlButt;
	@Wire
	Button pngButt;
	@Wire
	Button dropAnimationButt;
	@Wire
	Button dropAnimationButt2;
	@Wire
	Button kmlRemButt;
	@Wire
	Button pngRemButt;
	
	

	
	@Override
    public void doAfterCompose(Component component) throws Exception {
        super.doAfterCompose(component);
        
        // Init my SFMapControl component
        this.mapControl = new SFMapControl(this.gmaps);
        this.gmaps.setCenter(50.246705, 15.839178);  
        
        //THIS DOESNT WORK !!!! BUT FOR EXAMPLE ON LINE 128 THE SAME METOD WORKS, AFTER CLICK ON BUTTON :(
        this.mapControl.setStreetViewControll(false);
        this.mapControl.setCrossCursor(true);
        this.mapControl.setPOI(!this.mapControl.isPOI());
    
	}
	
	@Listen("onMapClick=#gmaps")
    public void onMapClick(MapMouseEvent event) {
       //this.areaPolygon.addPath(event.getLatLng());
    }
	
	@Listen("onClick=#streetButt")
    public void doButt() {
		this.mapControl.setStreetViewControll(!this.mapControl.isStreetViewControll());
    }
	
	@Listen("onClick=#addMyGmarker")
    public void doAddGmarker() {
		if(this.gm2==null){
			this.gm2 = new Gmarker("hovno", 50.246705, 15.839178); 
			this.gm2.setParent(this.gmaps);
		}
		this.mapControl.setMarkerAnimationBound(this.gm2);
    }
	
	@Listen("onClick=#dropAnimationButt")
    public void doAnimationButt() {
		if(this.gm3==null){
			this.gm3 = new Gmarker("hovno", 41.8, -87.5);
			this.gm3.setParent(this.gmaps);
		}
		if(this.gm4==null){
			this.gm4 = new Gmarker("hovno", 41.5, -87.5);
			this.gm4.setParent(this.gmaps);
		}
		this.mapControl.setMarkerAnimationDrop(gm3);
		this.mapControl.setMarkerAnimationDrop(gm4);
    }
	@Listen("onClick=#dropAnimationButt2")
    public void doAnimationButt2() {
		if(this.gm4==null){
			this.gm4 = new Gmarker("hovno", 41.5, -87.5);
			this.gm4.setParent(this.gmaps);
		}
		//this.mapControl.setMarkerAnimationDrop(gm4);
    }
	
	@Listen("onClick=#stopBound")
    public void stopBound() {
        //this.mapControl.removeAnimation(this.gm);
        this.mapControl.removeAnimation(this.gm2);
    }
	
	// THIS WORKS!!!!!
	@Listen("onClick=#poiButt")
    public void doPoi() {
        this.mapControl.setPOI(!this.mapControl.isPOI());
    }
	@Listen("onClick=#crossButt")
    public void doCross() {
        this.mapControl.setCrossCursor(!this.mapControl.isCrossCursor());
    }
	
	@Listen("onClick=#kmlButt")
    public void doKml() {
        this.mapControl.setKMLLayer("http://gmaps-samples.googlecode.com/svn/trunk/ggeoxml/cta.kml");
    }
    @Listen("onClick=#kmlRemButt")
    public void doKmlrem() {
        this.mapControl.removeKMLLayer();
    }
	
	@Listen("onClick=#pngButt")
    public void doPng() {
        this.mapControl.setPNGLayer("http://www.kasl-bazeny.cz/mapa2/");
    }
    
    @Listen("onClick=#pngRemButt")
    public void doPngRem() {
    	//TODO
        this.mapControl.removePNGLayer();
    }

}
