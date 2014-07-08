/**
 * 
 * Base naming rule: The stuff start with "_" means private , end with "_" means
 * protect , others mean public.
 * 
 * All the member field should be private.
 * 
 * Life cycle: (It's very important to know when we bind the event) A widget
 * will do this by order : 1. $init 2. set attributes (setters) 3. rendering
 * mold (@see mold/gmapsextension.js ) 4. call bind_ to bind the event to dom .
 * 
 * this.deskop will be assigned after super bind_ is called, so we use it to
 * determine whether we need to update view manually in setter or not. If
 * this.desktop exist , means it's after mold rendering.
 * 
 */
gmapsextension.SFMapControl = zk
		.$extends(zul.Widget,
				{
				_POI: true,

					/**
					 * Don't use array/object as a member field, it's a
					 * restriction for ZK object, it will work like a static ,
					 * share with all the same Widget class instance.
					 * 
					 * if you really need this , assign it in bind_ method to
					 * prevent any trouble.
					 * 
					 * TODO:check array or object , must be one of them ...I
					 * forgot. -_- by Tony
					 */

					$define : {
						/**
						 * The member in $define means that it has its own
						 * setter/getter. (It's a coding sugar.)
						 * 
						 * If you don't get this , you could see the comment
						 * below for another way to do this.
						 * 
						 * It's more clear.
						 * 
						 */
						mapId : function(v) {
							var map;
							map = this._map = zk.Widget.$('#' + v);
						},
						
						crossCursor: function(b) {
							var maps = this._map._gmaps;
							if (maps && b) {
									maps.setOptions({ draggableCursor: 'url(http://geoportal.gisqatar.org.qa/qmap/assets/images/cross_cursor.png), auto' });
							}else if(maps && !b){
									maps.setOptions({ draggableCursor: null });
							}
						},

						streetView : function(b) {
							var maps = this._map._gmaps;

							if (maps) {
								maps.setOptions({
									streetViewControl : b
								});
							}
						},

						POI : function(g) {
							var maps = this._map._gmaps, 
							opts = this.getMapOptions();
							
							if(g){
								var myStyles = [ {
									featureType : "poi",
									elementType : "all",
									stylers : [ {
										visibility : "on"
									} ]
									} ];
							}else{
								var myStyles = [ {
									featureType : "poi",
									elementType : "all",
									stylers : [ {
										visibility : "off"
									} ]
									} ];
							}
							
							if (maps) {
								if(opts){
									opts.styles = myStyles;
									maps.setOptions(opts);
								}
							}
						},
						
					    kmllayer: function(p) {
							var maps = this._map._gmaps;
							
							if (maps && p) {
								 _ctaLayer = new google.maps.KmlLayer({url: p});
						     	 _ctaLayer.setMap(maps);
						    }else if(maps && (p=="")){
						     		 _ctaLayer.setMap(null);
						     	 }
						},

						pngLayer: function(p) {
							var maps = this._map._gmaps,
							    mapOptions = {
								    zoom: 15,
								    center: new google.maps.LatLng(50.247858, 15.840172)
								  };
							   
								maps.setOptions(mapOptions);
								
								if (maps && (p=="")) {
									//TODO
									//maps.overlayMapTypes.clear();
								}else if(maps){
									var imageMapType = new google.maps.ImageMapType({
									    getTileUrl: function(coord, zoom) {
									      return p + zoom + "/" + coord.x + "/" + coord.y + ".png";
									    },
									    tileSize: new google.maps.Size(256, 256)
									  });
									  maps.overlayMapTypes.push(imageMapType);
									
								}
								
						},

						
					},
					/**
					 * If you don't like the way in $define , you could do the
					 * setter/getter by yourself here.
					 * 
					 * Like the example below, they are the same as we mentioned
					 * in $define section.
					 */
					// getters and setters
					setAnimationBound : function(g) {
						var marker = g.mapitem_;
						if (marker) {
							marker.setAnimation(google.maps.Animation.BOUNCE);
						}
						
						setTimeout(function() {
							marker.setAnimation(google.maps.Animation.DROP);
							}, 3000);
						  
					},
					
					setAnimationDrop : function(g) {
						var marker = g.mapitem_;
						if (marker) {
							marker.setAnimation(google.maps.Animation.DROP);
						}
					},

					setRemoveAnimation : function(g) {
						var marker = g.mapitem_;
						if (marker) {
							marker.setAnimation(null);
						}
					},
					
					
					// class methods
					getMapOptions : function() {
						if (!this._mapOptions) {
							// default not support contain map type
							var mtids = [];
							this._mapOptions = {
								// used to be {type: G_PHYSICAL_MAP}
								mapTypeControlOptions : {
									mapTypeIds : mtids
								}
							};
							// alert("dostal jsem map options");
						}
						return this._mapOptions;
					},

					bind_ : function() {
						/**
						 * For widget lifecycle , the super bind_ should be
						 * called as FIRST STATEMENT in the function. DONT'T
						 * forget to call supers in bind_ , or you will get
						 * error.
						 */
						this.$supers(gmapsextension.SFMapControl, 'bind_',
								arguments);
						this._tryBind();

						// A example for domListen_ , REMEMBER to do domUnlisten
						// in unbind_.
						// this.domListen_(this.$n("cave"), "onClick",
						// "_doItemsClick");
					},

					_tryBind : function() {
						var mapId;
						// init if google api, mapId and panelId are ready
						if (window.google && window.google.maps
								&& (mapId = this._mapId))
							this._init();
						else if ((mapId = this._mapId)) {
							// retry if the info for init is ready
							var wgt = this;
							setTimeout(function() {
								wgt._tryBind()
							}, 100);
						}
					},

					_init : function() {
						var mapId = this._mapId, map;

						if (!(map = this._map))
							map = this._map = this._mapa = zk.Widget.$('#'
									+ mapId);

						// while map are ready
						if (map && map._gmaps) {
							map = this._map._gmaps;
								// default remove POI and streetview
							var myStyles = [ {
								featureType : "poi",
								elementType : "all",
								stylers : [ {
									visibility : "off"
								} ]
								} ];
							this.setPOI(this._POI, {force:true});
//							map.setOptions({
//									streetViewControl : false,
//									styles: myStyles
//								});
							//map.setOptions({ draggableCursor: 'url(http://geoportal.gisqatar.org.qa/qmap/assets/images/cross_cursor.png), auto' });
							
						} else if (mapId) {
							// retry if the info for routing is ready
							var wgt = this, timer;
							if (timer = this._initTimer)
								clearTimeout(timer);
							this._initTimer = setTimeout(function() {
								wgt._init()
							}, 100);
						}
					},
					/*
					 * A example for domListen_ listener.
					 */
					/*
					 * _doItemsClick: function (evt) { alert("item click event
					 * fired"); },
					 */
					unbind_ : function() {

						// A example for domUnlisten_ , should be paired with
						// bind_
						// this.domUnlisten_(this.$n("cave"), "onClick",
						// "_doItemsClick");

						/*
						 * For widget lifecycle , the super unbind_ should be
						 * called as LAST STATEMENT in the function.
						 */
						this.$supers(gmapsextension.SFMapControl, 'unbind_',
								arguments);
					},
					/*
					 * widget event, more detail please refer to
					 * http://books.zkoss.org/wiki/ZK%20Client-side%20Reference/Notifications
					 */
					doClick_ : function(evt) {
						this.$super('doClick_', evt, true);// the super
						// doClick_ should
						// be
						// called
						this.fire('onFoo', {
							foo : 'myData'
						});
					},

					getZclass : function() {
						return this._zclass != null ? this._zclass
								: "z-sfmapcontrol";
					}
				});