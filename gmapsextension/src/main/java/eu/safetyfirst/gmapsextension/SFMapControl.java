package eu.safetyfirst.gmapsextension;

import java.util.Map;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;

public class SFMapControl extends XulElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static {
		addClientEvent(SFMapControl.class, "onFoo", 0);
	}

	/* Here's a simple example for how to implements a member field */

	private String _mapId;
	private Gmaps _map;
	private boolean _streetViewControll = true;
	private boolean _POI = true;
	private boolean _crossCursor = false;
	private boolean _kmlLayer = false;
	private boolean _pngLayer = false;

	
	//Constructor
	
	public SFMapControl() {
		super();
	}
	
	public SFMapControl(Gmaps map) {
		super();
		this.setMap(map);
		this.setParent(map.getParent());
	}
	
	
	// Calling methods to SFMapControl.js
 
	public void setMarkerAnimationBound(Gmarker gmarker) {
		if (gmarker != null)
			smartUpdate("animationBound", gmarker);
	}

	public void removeAnimation(Gmarker gmarker) {
		if (gmarker != null)
			smartUpdate("removeAnimation", gmarker);
	}
	
	public void setMarkerAnimationDrop(Gmarker gmarker) {
		if (gmarker != null)
			smartUpdate("animationDrop", gmarker);
	}

	public void setStreetViewControll(boolean streetView) {
		smartUpdate("streetView", streetView);
		this._streetViewControll = streetView;
	}

	public boolean isStreetViewControll() {
		return this._streetViewControll;
	}

	public void setPOI(boolean POI) {
		smartUpdate("POI", POI);
		this._POI = POI;
	}

	public boolean isPOI() {
		return this._POI;
	}

	public void setCrossCursor(boolean crossCursor) {
		smartUpdate("crossCursor", crossCursor);
		this._crossCursor = crossCursor;
	}

	public boolean isCrossCursor() {
		return this._crossCursor;
	}

	public void setKMLLayer(String kmlPath) {
		smartUpdate("kmllayer", encodeURL(kmlPath));
		this._kmlLayer = true;
	}
	
	public boolean removeKMLLayer() {
		if(this._kmlLayer == true)
			smartUpdate("kmllayer", "");
		return this._kmlLayer = false;
	}
	
	
	public void setPNGLayer(String pngPath) {
		smartUpdate("pngLayer", encodeURL(pngPath));
		this._pngLayer = true;
	}
	
	public boolean removePNGLayer() {
		//TODO
		smartUpdate("pngLayer", "");
		return this._pngLayer = false;
	}

	private String encodeURL(String url) {
		final Desktop desktop = getDesktop();
		return (desktop == null ? Executions.getCurrent() : desktop
				.getExecution()).encodeURL(url);
	}

	/**
	 * Set map to google direction display, note you should make sure the map is
	 * already attached to page before call this function because the uuid will
	 * be changed when attache to page
	 * 
	 * @param map
	 */
	public void setMap(Gmaps map) {
		_map = map;
		setMapId(map.getUuid());
	}

	/**
	 * set map id directly
	 * 
	 * @param mapId
	 */
	public void setMapId(String mapId) {
		_mapId = mapId;
		smartUpdate("mapId", _mapId);
	}

	/**
	 * get map id
	 * 
	 * @return
	 */
	public String getMapId() {
		return _mapId;
	}

	// super//
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
			throws java.io.IOException {
		super.renderProperties(renderer);
		if (_mapId != null)
			render(renderer, "mapId", _mapId);  
		
		if (!_POI)
			render(renderer, "POI", isPOI());  
		
		//if (!_POI)
		//	renderer.render("POI", isPOI());
	}

	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		final Map data = request.getData();

		if (cmd.equals("onFoo")) {
			final String foo = (String) data.get("foo");
			System.out.println("do onFoo, data:" + foo);
			Events.postEvent(Event.getEvent(request));
		} else
			super.service(request, everError);
	}

	/**
	 * The default zclass is "z-sfmapcontrol"
	 */
	public String getZclass() {
		return (this._zclass != null ? this._zclass : "z-sfmapcontrol");
	}
}
