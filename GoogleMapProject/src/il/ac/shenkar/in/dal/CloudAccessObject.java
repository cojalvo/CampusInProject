package il.ac.shenkar.in.dal;

import il.ac.shenkar.common.CampusInConstant;
import il.ac.shenkar.common.CampusInEvent;
import il.ac.shenkar.common.CampusInLocation;
import il.ac.shenkar.common.CampusInMessage;
import il.ac.shenkar.common.CampusInUser;
import il.ac.shenkar.common.CampusInUserLocation;
import il.ac.shenkar.common.ParsingHelper;
import il.ac.shenkar.common.PersonalSettings;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.Drawable;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.google.android.gms.internal.cm;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CloudAccessObject implements IDataAccesObject {
	// single tone
	private static CloudAccessObject instace;
	private CampusInUser curentCampusInUser = null;
	private Date eventsLastUpate;
	private Date messagesLastUpdate;
	private ParseObject parseCurrentCampusInUser = null;
	private Drawable profilePic = null;
	private Date allUsersLastUpdate;
	private Date usersLastUpdate;
	private Date usersLocationLastUpdate;
	private List<CampusInUser> allUsersList;

	private CloudAccessObject() {

	}

	public static CloudAccessObject getInstance() {
		if (instace == null)
			instace = new CloudAccessObject();
		return instace;
	}

	@Override
	public void getEvents(
			final DataAccesObjectCallBack<List<CampusInEvent>> callBack) {
		// load the currentCampusiN user just in case it wasnt loaded yet.
		loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {

			@Override
			public void done(CampusInUser retObject, Exception e) {
				if (e == null) {
					ParseQuery<ParseObject> dateQuery = ParseQuery
							.getQuery("Event");
					dateQuery.whereGreaterThanOrEqualTo("date", new Date());

					ParseQuery<ParseObject> ownerQuery = ParseQuery
							.getQuery("Event");
					ownerQuery.whereEqualTo("ownerParseId",
							curentCampusInUser.getParseUserId());

					ParseQuery<ParseObject> recieverQuery = ParseQuery
							.getQuery("Event");
					recieverQuery.whereEqualTo("recivers",
							curentCampusInUser.getParseUserId());

					ParseQuery<ParseObject> isPublicQuery = ParseQuery
							.getQuery("Event");
					isPublicQuery.whereEqualTo("isPublic", true);

					List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
					queries.add(isPublicQuery);
					queries.add(recieverQuery);
					queries.add(ownerQuery);
					ParseQuery<ParseObject> orQuery = ParseQuery.or(queries);

					// this will set the date as and logic ---> (date &&
					// (ownerId||reciverId||isPublic))
					orQuery.whereGreaterThanOrEqualTo("date", new Date());

					orQuery.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> resList,
								ParseException e) {
							List<CampusInEvent> eventList = new ArrayList<CampusInEvent>();
							if (e == null) {
								for (ParseObject parseEvent : resList) {

									eventList
											.add(createCampusInEventFromParseObj(parseEvent));
								}
							}
							if (callBack != null)
								callBack.done(eventList, e);
						}
					});

				}

			}
		});

	}

	private CampusInEvent createCampusInEventFromParseObj(ParseObject parseObj) {
		// ParseObject theEvent=new ParseObject("Event");
		// theEvent.add("title", event.getHeadLine());
		// theEvent.add("description", event.getDescription());
		// theEvent.add("date", event.getDate());
		// theEvent.add("locationName",event.getLocation().getName());
		// theEvent.add("lat", event.getLocation().getMapLocation().latitude);
		// theEvent.add("long", event.getLocation().getMapLocation().longitude);
		// theEvent.add("isPublic",event.isGlobal());
		// theEvent.add("ownerParseId", event.getOwnerId());
		// theEvent.add("type", event.getEventType());
		// for (String reciverId : event.getReceiversId()){
		// theEvent.add("recivers", reciverId);
		CampusInEvent event = null;
		if (parseObj != null) {
			event = new CampusInEvent();
			event.setLocation(new CampusInLocation());
			// event.setDate(parseObj.getDate("date"));
			event.setHeadLine(parseObj.getString("title"));
			event.setDescription(parseObj.getString("description"));
			event.getLocation().setMapLocation(
					new LatLng(parseObj.getDouble("lat"), parseObj
							.getDouble("long")));
			event.getLocation().setLocationName(parseObj.getString("locationName"));
			event.setGlobal(parseObj.getBoolean("isPublic"));
			event.setOwnerId(parseObj.getString("ownerParseId"));
			event.setReciversList(new ArrayList<String>());
			for (Object reciverId : parseObj.getList("recivers")) {
				event.getReceiversId().add(reciverId.toString());
			}
		}
		return event;
	}

	@Override
	public void getMessages(
			DataAccesObjectCallBack<List<CampusInMessage>> callBack) {

	}

	@Override
	public void sendMessage(CampusInMessage message,
			DataAccesObjectCallBack<Integer> callBack) {

	}

	@Override
	public void sendEvent(CampusInEvent event,
			final DataAccesObjectCallBack<Integer> callback) {
		if (event != null) {
			ParseObject theEvent = new ParseObject("Event");
			theEvent.put("title", event.getHeadLine());
			theEvent.put("description", event.getDescription());
			theEvent.put("date", event.getDate());
			theEvent.put("locationName", event.getLocation().getLocationName());
			theEvent.put("lat", event.getLocation().getMapLocation().latitude);
			theEvent.put("long", event.getLocation().getMapLocation().longitude);
			theEvent.put("isPublic", event.isGlobal());
			theEvent.put("ownerParseId", event.getOwnerId());
			theEvent.put("type", event.getEventType());
			for (String reciverId : event.getReceiversId()) {
				theEvent.add("recivers", reciverId);
			}
			theEvent.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (callback != null) {
						if (e == null)
							callback.done(0, e);
						else
							callback.done(1, e);
					}

				}
			});

		}

	}

	@Override
	public void updateLocation(final CampusInLocation userLoction,
			final DataAccesObjectCallBack<Integer> callBack) {
		loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {

			@Override
			public void done(CampusInUser retObject, Exception e) {
				// try to load the location object from parse
				if (retObject == null) {
					callBack.done(1, new Exception(
							"Unble to load campusInUser from the cloud"));
					return;
				}
				ParseQuery<ParseObject> query = ParseQuery
						.getQuery("UserLocation");
				query.whereEqualTo("faceBookUserID",
						curentCampusInUser.getFaceBookUserId());
				query.findInBackground(new FindCallback<ParseObject>() {
					public void done(List<ParseObject> scoreList,
							ParseException e) {
						if (e == null) {
							// if the user exist in the cloud then the size of
							// the
							// return list will be 1
							if (scoreList.size() == 1) {
								if (userLoction != null) {
									scoreList.get(0).remove("lat");
									scoreList
											.get(0)
											.put("lat",
													userLoction
															.getMapLocation().latitude);
									scoreList.get(0).remove("lon");
									scoreList
											.get(0)
											.put("lon",
													userLoction
															.getMapLocation().longitude);
									scoreList.get(0).remove("locationName");
									scoreList.get(0).put("locationName",
											userLoction.getLocationName());
								}
								scoreList.get(0).saveInBackground(
										new SaveCallback() {
											@Override
											public void done(ParseException e) {
												if (e != null) {
													Log.e("cadan",
															"Upadet location was failed");
												}
												callBack.done(0, e);
												return;
											}
										});
							}
							// if the user location info doesn't exist in the
							// cloud than
							// report it.
							else {
								loadParseCurrentCampusInUser(new DataAccesObjectCallBack<ParseObject>() {

									@Override
									public void done(ParseObject retObject,
											Exception e) {
										if (retObject == null) {
											callBack.done(
													1,
													new Exception(
															"Unable to load parseCuttentUser"));
											return;
										}
										final ParseObject po = new ParseObject(
												"UserLocation");
										po.put("parseUserID",
												curentCampusInUser
														.getParseUserId());
										po.put("faceBookUserID",
												curentCampusInUser
														.getFaceBookUserId());
										po.put("lat",
												userLoction.getMapLocation().latitude);
										po.put("lon",
												userLoction.getMapLocation().longitude);
										po.put("locationName",
												userLoction.getLocationName());
										// add the parse current user to the
										// relation 1
										// to 1 relation
										po.saveInBackground(new SaveCallback() {
											@Override
											public void done(ParseException e2) {
												if (e2 != null) {
													Log.e("cadan",
															"Upadet location was failed");
													callBack.done(1, e2);
													return;
													// add the location object
													// to the
													// relation of
													// current campus in user of
													// pars
												}
												po.getRelation(
														"userlocationRelation")
														.add(parseCurrentCampusInUser);
												po.saveInBackground(new SaveCallback() {

													@Override
													public void done(
															ParseException e3) {
														// TODO Auto-generated
														// methode3 stub
														if (e3 == null)
															callBack.done(0, e3);
														else
															callBack.done(1, e3);

													}
												});

											}
										});

									}
								});
							}

						} else {
							// the query was failed, update the observers.
							// updateObserves(ActionCode.UserLocation, false);
						}
					}
				});

			}
		});

	}

	/*
	 * get all users location from the cloud
	 * 
	 * @see
	 * il.ac.shenkar.in.dal.IDataAccesObject#getUsersInBackground(il.ac.shenkar
	 * .in.dal.DataAccesObjectCallBack)
	 */
	@Override
	public void getUsersLocationInBackground(
			final DataAccesObjectCallBack<List<CampusInUserLocation>> callBack) {

		// load current campus in user in case it wasnt loaded yet
		loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {

			@Override
			public void done(CampusInUser retObject, Exception e) {
				if (e == null)

				{
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("CampusInUser");
					query.whereEqualTo("parseUserId",
							curentCampusInUser.getFaceBookUserId());
					query.include("CampusInUser");
					query.include("UserLocation");
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> userList,
								ParseException e) {
							List<CampusInUserLocation> result = new ArrayList<CampusInUserLocation>();
							for (ParseObject parseObject : userList) {
								result.add(getCampusInUserLocationFromParseObject(parseObject));
							}
							callBack.done(result, e);
						}
					});
				}

			}
		});

	}

	@Override
	public void getAllUsersInBackground(
			DataAccesObjectCallBack<List<CampusInUser>> callBack) {
		// TODO Auto-generated method stub

	}

	/*
	 * Get Parse obj and return CampusInObj
	 */
	private CampusInUserLocation getCampusInUserLocationFromParseObject(
			ParseObject paramParseObject) {
		CampusInUserLocation localCampusInUserLocation = null;
		if (paramParseObject != null) {
			LatLng localLatLng = new LatLng(paramParseObject.getDouble("lat"),
					paramParseObject.getDouble("lon"));
			localCampusInUserLocation = new CampusInUserLocation();
			localCampusInUserLocation.setUser(new CampusInUser());
			localCampusInUserLocation.getUser().setParseUserId(
					paramParseObject.getString("parseUserId"));
			localCampusInUserLocation.getUser().setFaceBookUserId(
					paramParseObject.getString("facebookId"));
			localCampusInUserLocation.getUser().setFirstName(
					paramParseObject.getString("firstName"));
			localCampusInUserLocation.getUser().setLastName(
					paramParseObject.getString("lastName"));
			localCampusInUserLocation.setLocation(new CampusInLocation());
			localCampusInUserLocation.getLocation().setMapLocation(localLatLng);
			localCampusInUserLocation.getLocation().setLocationName(
					paramParseObject.getString("locationName"));
			/*localCampusInUserLocation.getLocation().setDate(
					paramParseObject.getUpdatedAt());*/		}
		return localCampusInUserLocation;
	}

	private void loadParseCurrentCampusInUser(
			final DataAccesObjectCallBack<ParseObject> callBack) {
		if (this.parseCurrentCampusInUser != null) {
			if (callBack != null)
				callBack.done(this.parseCurrentCampusInUser, null);
			return;
		}
		ParseQuery<ParseObject> localParseQuery = ParseQuery
				.getQuery("CampusInUser");
		localParseQuery.whereEqualTo("parseUserId", ParseUser.getCurrentUser()
				.getObjectId());
		localParseQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> retList, ParseException e) {
				if (e == null && retList != null && retList.size() == 1) {
					CloudAccessObject.this.parseCurrentCampusInUser = retList
							.get(0);
					if (callBack != null)
						callBack.done(parseCurrentCampusInUser, e);
				} else {
					if (callBack != null)
						callBack.done(
								null,
								new Exception(
										"Current campus in user is not yet in the cloude"));
				}
			}
		});

	}

	/*
	 * add cumpus in user to the friend list of the current user(non-Javadoc)
	 * 
	 * @see
	 * il.ac.shenkar.in.dal.IDataAccesObject#addFriendToFriendList(il.ac.shenkar
	 * .common.CampusInUser, il.ac.shenkar.in.dal.DataAccesObjectCallBack)
	 */
	public void addFriendToFriendList(CampusInUser userToAdd,
			final DataAccesObjectCallBack<Integer> callBack) {
		if (userToAdd == null) {
			callBack.done(Integer.valueOf(1), new Exception(
					"User to add is null"));
			return;
		}
		ParseQuery<ParseObject> query = ParseQuery.getQuery("CampusInUser");
		query.whereEqualTo("parseUserId", userToAdd.getParseUserId());
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> retList, ParseException arg1) {
				if (retList.size() != 1) {
					callBack.done(Integer.valueOf(1), new Exception(
							"This user is not exist in CampusIN"));
					return;
				}
				ParseObject localParseObject = (ParseObject) retList.get(0);
				ParseUser.getCurrentUser().getRelation("friends")
						.add(localParseObject);
				ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
					public void done(
							ParseException paramAnonymous2ParseException) {
						if (callBack != null)
							callBack.done(Integer.valueOf(0),
									paramAnonymous2ParseException);
					}
				});
			}
		});
		{

		}

	}

	@Override
	public void loadCurrentCampusInUser(
			final DataAccesObjectCallBack<CampusInUser> callBack) {
		if (this.curentCampusInUser == null) {
			// load the parse object that contain the campus in user
			loadParseCurrentCampusInUser(new DataAccesObjectCallBack<ParseObject>() {

				@Override
				public void done(ParseObject retObj, Exception e) {
					// if the obj was found then it is not the first time
					if (e == null && retObj != null) {
						// fetch rhe data from the parse obj
						curentCampusInUser = fromParseObjToCampusInUser(retObj);
						// return the callback
						if (callBack != null)
							callBack.done(curentCampusInUser, e);
						return;
					}
					// if nothing was found or an error accured than craete the
					// cumpusInUser with minimum data
					// later when the user will login the data should be insert
					else {

						curentCampusInUser = new CampusInUser();
						FacebookServices.makeMeRequest(
								ParseFacebookUtils.getSession(),
								new Request.GraphUserCallback() {
									public void onCompleted(
											GraphUser paramAnonymousGraphUser,
											Response paramAnonymousResponse) {
										CloudAccessObject.this.curentCampusInUser
												.setFaceBookUserId(paramAnonymousGraphUser
														.getId());
										CloudAccessObject.this.curentCampusInUser
												.setFirstName(paramAnonymousGraphUser
														.getFirstName());
										CloudAccessObject.this.curentCampusInUser
												.setLastName(paramAnonymousGraphUser
														.getLastName());
										CloudAccessObject.this.curentCampusInUser
												.setParseUserId(ParseUser
														.getCurrentUser()
														.getObjectId());
										if (callBack != null)
											callBack.done(
													CloudAccessObject.this.curentCampusInUser,
													null);
									}
								});
					}
				}

			});
		} else {
			callBack.done(curentCampusInUser, null);
		}
	}

	private CampusInUser fromParseObjToCampusInUser(ParseObject obj) {
		CampusInUser user = new CampusInUser();

		// TODO maybe throw an exception
		if (obj != null) {
			user.setFaceBookUserId(obj.getString("facebookId"));
			user.setTrend(obj.getString("trend"));
			user.setFirstName(obj.getString("firstName"));
			user.setLastName(obj.getString("lastName"));
			user.setParseUserId(obj.getString("parseUserId"));
			user.setYear(obj.getString("year"));
		}
		return user;
	}

	@Override
	public void putCurrentCampusInUserInbackground(
			final CampusInUser currentCampusInUser,
			final DataAccesObjectCallBack<Integer> callBack) 
	{
		ParseQuery<ParseObject> query =ParseQuery.getQuery("CampusInUser");
		query.whereEqualTo("parseUserId", currentCampusInUser.getParseUserId());
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> retObj, ParseException e) {
				if(e==null&& retObj!=null)
				{
					//cretate new one 
					if(retObj.size()==0)
					{
						final ParseObject currentUserParseObject = new ParseObject(
								"CampusInUser");
						currentUserParseObject.put("firstName",
								currentCampusInUser.getFirstName());
						currentUserParseObject.put("lastName",
								currentCampusInUser.getLastName());
						currentUserParseObject.put("facebookId",
								currentCampusInUser.getFaceBookUserId());
						currentUserParseObject.put("parseUserId",
								currentCampusInUser.getParseUserId());
						currentUserParseObject.put("trend", currentCampusInUser.getTrend());
						currentUserParseObject.put("year", currentCampusInUser.getYear());
						currentUserParseObject.saveInBackground(new SaveCallback() {
							public void done(ParseException e) {
								CloudAccessObject.this.parseCurrentCampusInUser = currentUserParseObject;
							}
						});
					}
					callBack.done(Integer.valueOf(0), e);
				}
				
			}
		});
	
	}

	public void getProfilePicture(
			final DataAccesObjectCallBack<Drawable> callBack) {
		if (this.profilePic != null) {
			callBack.done(this.profilePic, null);
			return;
		}
		FacebookServices.getPictureForFacebookId(
				this.curentCampusInUser.getFaceBookUserId(),
				new DataAccesObjectCallBack<Drawable>() {
					@Override
					public void done(Drawable retPic, Exception e) {
						if (e == null) {
							CloudAccessObject.this.profilePic = retPic;
							callBack.done(CloudAccessObject.this.profilePic, e);
						}
					}
				});
	}

	@Override
	public void getCurrentUserFriendsToScool(
			final DataAccesObjectCallBack<List<CampusInUser>> callBack) {
		loadCurrentCampusInUser(new DataAccesObjectCallBack<CampusInUser>() {

			@Override
			public void done(CampusInUser retObject, Exception e) {
				ParseQuery<ParseObject> query = ParseQuery
						.getQuery("CampusInUser");
				query.whereEqualTo("trend", curentCampusInUser.getTrend());
				query.whereEqualTo("year", curentCampusInUser.getYear());
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> retList, ParseException e) {
						ArrayList<CampusInUser> friendList = new ArrayList<CampusInUser>();
						if (e == null) {

							for (ParseObject parseObject : retList) {
								friendList
										.add(fromParseObjToCampusInUser(parseObject));
							}
						}
						if (callBack != null)
							callBack.done(friendList, e);
					}
				});
			}
		});

	}

	
	@Override
	public void getAllCumpusInUsers(
			final DataAccesObjectCallBack<List<CampusInUser>> callBack) {
		// first load
		ParseQuery<ParseObject> query = ParseQuery.getQuery("CampusInUser");
		if (allUsersList == null) {
			allUsersList = new ArrayList<CampusInUser>();
		}
		else
		{
			query.whereGreaterThanOrEqualTo("createdAt",allUsersLastUpdate);
		}
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> retList, ParseException e) {
				if (e == null && retList != null) {
					allUsersLastUpdate = new Date();
					for (ParseObject parseObject : retList) {
						allUsersList
								.add(fromParseObjToCampusInUser(parseObject));
					}
					if (callBack != null) {
						callBack.done(allUsersList, e);
					}
				}

			}
		});
	}

	@Override
	public void getAllCampusInUsersStartWith(
			DataAccesObjectCallBack<List<CampusInUser>> callBack) {
		// TODO Auto-generated method stub
		
	}
}
