package com.example.parenteye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/*
Activity log adapter
clever
 */
public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {


    final long ONE_MEGABYTE = 1024 * 1024;

    // initialize firebase connection
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    //initialize pure  context
    private Context mContext;

    // initialize list of Notifications
    private List<ActivityLog> mActivityLog;


    private StorageReference UStorageRef;
    private StorageReference PStorageRef;
    private StorageReference GroupStorageRef;
    private StorageReference pageStorageRef;
    public static final String searched_user_Id="searched_user_Id";
    public static final String specific_Post_Id="specific_Post_Id";
    public static final String searched_page_Id="searched_page_Id";
    public static final String searched_group_Id="searched_group_Id";
    public static final String searched_Item_name="searched_Item_name";

    //NotificationAdapter constructor
    public ActivityLogAdapter(Context mContext, List<ActivityLog> mActivityLog) {
        this.mContext = mContext;
        this.mActivityLog = mActivityLog;
    }


    // onCreate function of NotificationAdapter
    // the  return type is the sub class (ViewHolder) which take and hold the layout of (notification_item Activity)
    @NonNull
    @Override
    public ActivityLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View notification_root_view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);


        return new ActivityLogAdapter.ViewHolder(notification_root_view);
    }


    /*
       onBindViewHolder function of NotificationAdapter
         means  add notification from data base to the layout of (notification_item Activity)
         take the view of notification_item Activity and notification number or ID (i)
         get data from notification database after pushed by the number of notification in the BD and put it into the layout
   */
    @Override
    public void onBindViewHolder(@NonNull ActivityLogAdapter.ViewHolder viewHolder, int position) {


        //initialize and get notification
        final ActivityLog activityLog = mActivityLog.get(position);

        viewHolder.Holder_notification_content_text.setText(activityLog.getActivityLogMessage());
        //put the notification text into the content holder
        final String childId = activityLog.getChildId();
        //call getuserInfo func to get the user name and profile pic of him
        getuserInfo(viewHolder.Holder_notification_profile_image, viewHolder.Holder_notification_user_name, childId);

        final String PostId = activityLog.getPostId();

        final String friendID = activityLog.getuserId();


        //getPostImage(viewHolder.Holder_notification_post_content ,viewHolder.Holder_notification_post_image, PostId);


        //check if the notification is a post
        if (activityLog.getIsPost()) {
            //if ispost is true photo will visible
            viewHolder.Holder_notification_post_image.setVisibility(View.VISIBLE);

            //friend request layout gone
            viewHolder.Holder_Friend_request_notifi_layout.setVisibility(View.GONE);

            //call  getPostImage func to get the post pic
            getPostImage(viewHolder.Holder_notification_post_content, viewHolder.Holder_notification_post_image, activityLog.getPostId());


        } else if(activityLog.getisFriendRequest()){
            // accept and reject lay out will gone
            viewHolder.Holder_Friend_request_notifi_layout.setVisibility(View.GONE);


            //if ispost is false photo of post and content will replace with photo of riend and the name of him

            getuserInfo(viewHolder.Holder_notification_post_image, viewHolder.Holder_notification_post_content,friendID);


        }else if (activityLog.getisGroupRequest()) {

            viewHolder.Holder_Friend_request_notifi_layout.setVisibility(View.GONE);

            String communityid = activityLog.getPostId();

            getCommunityInfo(null,viewHolder.Holder_notification_post_image,communityid);

            getuserInfo(null, viewHolder.Holder_notification_post_content,friendID);


        }else if (activityLog.getisPage()) {

            viewHolder.Holder_Friend_request_notifi_layout.setVisibility(View.GONE);

            String communityid = activityLog.getPostId();

            getCommunityInfo(viewHolder.Holder_notification_post_content, viewHolder.Holder_notification_post_image, communityid);
            //check about friend request

        }if(activityLog.getiscreategroup())
        {
            viewHolder.Holder_Friend_request_notifi_layout.setVisibility(View.GONE);

            String communityid = activityLog.getPostId();

            getCommunityInfo(viewHolder.Holder_notification_post_content, viewHolder.Holder_notification_post_image, communityid);
        }

                //************************** Activity log is post

        if (activityLog.getIsPost()) {
            //********************** action on the  post content in the list ***********************
            viewHolder.Holder_notification_post_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent specificPost_intent=new Intent(mContext,SpecificPostActivity.class);
                    specificPost_intent.putExtra(specific_Post_Id,PostId);
                    mContext.startActivity(specificPost_intent);
                    System.out.println("post id is  "+PostId);


                    // new Intent((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.notification_fragment_container,
                    //new custom_posts_returned()).commit();
                }
            });

            /*************************************** action on post image btn**************************
             *
             */
            viewHolder.Holder_notification_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent specificPost_intent=new Intent(mContext,SpecificPostActivity.class);
                    specificPost_intent.putExtra(specific_Post_Id,PostId);
                    mContext.startActivity(specificPost_intent);
                    System.out.println("post id is  "+PostId);
                }
            });


            /*************************************** action on child profile img btn**************************
             *
             */

            viewHolder.Holder_notification_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });
            /*************************************** action on child profile username btn**************************
             *
             */
            viewHolder.Holder_notification_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });


            //************************** Activity log is Friend request


        } else if(activityLog.getisFriendRequest()){




            /*************************************** action on post content used as user how request profile name btn**************************
             *
             */            viewHolder.Holder_notification_post_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,friendID);
                    mContext.startActivity(searched_intent);

                    // new Intent((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.notification_fragment_container,
                    //new custom_posts_returned()).commit();
                }
            });

            /*************************************** action on post image used as user how request profile img btn**************************
             *
             */
            viewHolder.Holder_notification_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,friendID);
                    mContext.startActivity(searched_intent);
                }
            });


            /*************************************** action on child profile img btn**************************
             *
             */

            viewHolder.Holder_notification_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
        Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });
            /*************************************** action on child profile username btn**************************
             *
             */
            viewHolder.Holder_notification_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });


            //********************************** id it activity on group


        }else if (activityLog.getisGroupRequest()) {

            //********************** action on the  post content used as name of group  ***********************

            viewHolder.Holder_notification_post_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,friendID);
                    mContext.startActivity(searched_intent);


                    // new Intent((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.notification_fragment_container,
                    //new custom_posts_returned()).commit();
                }
            });
            viewHolder.Holder_notification_content_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent searched_intent=new Intent(mContext,GroupActivity.class);
//                    searched_intent.putExtra(searched_group_Id,PostId);
//                    mContext.startActivity(searched_intent);
                }
            });
            /*************************************** action on post image used as image of group btn**************************
             *
             */
            viewHolder.Holder_notification_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,GroupActivity.class);
                    searched_intent.putExtra(searched_group_Id,PostId);
                    mContext.startActivity(searched_intent);
                }
            });


            /*************************************** action on child profile img btn**************************
             *
             */

            viewHolder.Holder_notification_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });
            /*************************************** action on child profile username btn**************************
             *
             */
            viewHolder.Holder_notification_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });



        }else if (activityLog.getisPage()) {

            //********************** action on the  post content used as name of page  ***********************

            viewHolder.Holder_notification_post_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent searched_intent=new Intent(mContext,PageActivity.class);
                    searched_intent.putExtra(searched_page_Id,PostId);
                    mContext.startActivity(searched_intent);

                    // new Intent((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.notification_fragment_container,
                    //new custom_posts_returned()).commit();
                }
            });

            /*************************************** action on post image used as image of page btn**************************
             *
             */
            viewHolder.Holder_notification_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent searched_intent=new Intent(mContext,PageActivity.class);
                    searched_intent.putExtra(searched_page_Id,PostId);
                    mContext.startActivity(searched_intent);
                }
            });


            /*************************************** action on child profile img btn**************************
             *
             */

            viewHolder.Holder_notification_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });
            /*************************************** action on child profile username btn**************************
             *
             */
            viewHolder.Holder_notification_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,childId);
                    mContext.startActivity(searched_intent);
                }
            });

        }if(activityLog.getiscreategroup())
        {
            viewHolder.Holder_notification_post_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searched_intent=new Intent(mContext,GroupActivity.class);
                    searched_intent.putExtra(searched_group_Id,PostId);
                    mContext.startActivity(searched_intent);
                }
            });


                    viewHolder.Holder_notification_post_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent searched_intent=new Intent(mContext,GroupActivity.class);
                            searched_intent.putExtra(searched_group_Id,PostId);
                            mContext.startActivity(searched_intent);
                        }
                    });
        }



             /*
            onClick func if pressed on the notification take action to go the post *****wants some trace and understanding*****
            */
        /*
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isPost()) {

                    SharedPreferences.Editor myPrefrences = mContext.getSharedPreferences("myPrefrences", Context.MODE_PRIVATE).edit();
                    myPrefrences.putString("Postid", notification.getPostId());
                    myPrefrences.apply();

                    //////////////////////////////////////////////


                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new custom_posts_returned()).commit();
                }else{

                        SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                        editor.putString("Profileid",notification.getUserId());
                        editor.apply();

                        //////////////////////////////////////////////
                        ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentProfile()).commit();

                }
            }
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mActivityLog.size();
    }


    //ViewHolder class thak and hold notification_item Activity
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView Holder_notification_profile_image, Holder_notification_post_image;
        public TextView Holder_notification_user_name, Holder_notification_content_text, Holder_notification_post_content;
        public LinearLayout Holder_Friend_request_notifi_layout;
        public Button Holder_Accept_notifi_btn,Holder_reject_notifi_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            Holder_notification_profile_image = itemView.findViewById(R.id.notification_profile_image_);
            Holder_notification_post_image = itemView.findViewById(R.id.notification_post_image_);
            Holder_notification_user_name = itemView.findViewById(R.id.notification_user_name_);
            Holder_notification_content_text = itemView.findViewById(R.id.notification_content_text_);
            Holder_notification_post_content = itemView.findViewById(R.id.post_content);


            Holder_Friend_request_notifi_layout = itemView.findViewById(R.id.Friend_request_notifi_layout);
            Holder_Accept_notifi_btn = itemView.findViewById(R.id.Accept_notifi_btn);
            Holder_reject_notifi_btn= itemView.findViewById(R.id.reject_notifi_btn);
        }
    }



             /*
                getuserInfo func gets the user name and profile pic of him
                 param: notification_post_image, notification_user_name to put the retrieved data in their places in the layout
                        ,notification.getUserId() which means user iD
              */

    private void getuserInfo(@Nullable final ImageView imageView, final TextView username, String whoMakeAction) {

        //String publisherid="cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
        DatabaseReference userReference = database.getReference("Users").child(whoMakeAction);

        UStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");


        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                if(imageView != null) {

                    if (user.getProfile_pic_id() != null) {
                        UStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(bm);
                            }
                        });
                    }
                }
                if(username != null) {

                    username.setText(user.getUsername());
                    //Glide.with(mContext).load(user.getProfile_pic_id()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*
              getPostImage func gets the post pic
               param: notification_post_image to put the retrieved data in their places in the layout
                     ,notification.getPostId()() which means post id
           */
    private void getPostImage(final TextView post_content, final ImageView imageView, String PostId) {
        //String postid="-LbU7gdO1Mkx4ZhulHxA";

        DatabaseReference postreference = database.getReference("Posts").child(PostId);

        PStorageRef = FirebaseStorage.getInstance().getReference("PostImages/");

        postreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Posts post = dataSnapshot.getValue(Posts.class);
                    if(post_content != null) {

                        if (post.getPostcontent() != null) {

                            post_content.setText(post.getPostcontent());
                        }
                        if (post.getImageId() != null) {
                            if(imageView!=null){
                                PStorageRef.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        imageView.setImageBitmap(bm);
                                    }
                                });
                            }

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
            ChildEventListener pchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Posts post = dataSnapshot.getValue(Posts.class);

                PStorageRef.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bm);
                    }
                });
                post_content.setText(post.getImageId());


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postreference.addChildEventListener(pchildEventListener);



*/



/*
            // Glide.with(mContext).load(post.getImageId()).into(imageView);

        });*/


    }

    /*
    private void getFriendRequestuserInfo(final ImageView imageView, final TextView username, String whoMakeAction) {

        //String publisherid="cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
        DatabaseReference userReference = database.getReference("Users").child(whoMakeAction);

        UStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");


        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if(user.getProfile_pic_id() !=null)
                {
                    UStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bm);
                        }
                    });
                }

                username.setText(user.getUsername());
                //Glide.with(mContext).load(user.getProfile_pic_id()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
*/

    private void getCommunityInfo(final TextView communityName, final ImageView imageView, String communityId) {
        //String postid="-LbU7gdO1Mkx4ZhulHxA";

        DatabaseReference groupreference = database.getReference("Community").child(communityId);

        GroupStorageRef = FirebaseStorage.getInstance().getReference("GroupImages/ ");
        pageStorageRef = FirebaseStorage.getInstance().getReference("PageImages/ ");


        groupreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Community community = dataSnapshot.getValue(Community.class);


                    if(community.getCommunityType().equalsIgnoreCase("page")) {

                        if(communityName != null)
                        {
                            communityName.setText(community.getCommunityname());

                        }

                        if(imageView != null)
                        {
                            if (community.getPhotoId() !=null){
                                PStorageRef.child(community.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        imageView.setImageBitmap(bm);
                                    }
                                });
                            }
                        }



                    }else if(community.getCommunityType().equalsIgnoreCase("group") ){
                        if(communityName != null)
                        {
                            communityName.setText(community.getCommunityname());

                        }

                        if(imageView != null) {
                            if (community.getCoverPhotoId() != null) {
                                PStorageRef.child(community.getCoverPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        imageView.setImageBitmap(bm);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
            ChildEventListener pchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Posts post = dataSnapshot.getValue(Posts.class);

                PStorageRef.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bm);
                    }
                });
                post_content.setText(post.getImageId());


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        postreference.addChildEventListener(pchildEventListener);



*/



/*
            // Glide.with(mContext).load(post.getImageId()).into(imageView);

        });*/


    }

    /*
    private void getFriendRequestuserInfo(final ImageView imageView, final TextView username, String whoMakeAction) {

        //String publisherid="cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
        DatabaseReference userReference = database.getReference("Users").child(whoMakeAction);

        UStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");


        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if(user.getProfile_pic_id() !=null)
                {
                    UStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bm);
                        }
                    });
                }

                username.setText(user.getUsername());
                //Glide.with(mContext).load(user.getProfile_pic_id()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
*/
}
