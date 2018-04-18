package com.meridian.dateout.login;

import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.RecyclerAdapterCategory;
import com.meridian.dateout.model.CategoryModel;
import com.meridian.dateout.model.DealsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String id, id1, category, background, icon;
    ArrayList<CategoryModel> categoryModelArrayList;
    ArrayList<DealsModel> dealsModelArrayList;
    ImageView img_account, img_collections, img_explore, img_chat;
    LinearLayout lay_account, lay_chat, lay_explore, lay_collections;
    String title;
    String image;
    String description;
    String discount;
    String timing;
    String delivery;
    String category1;
    String tags;
    String seller_id;
    ArrayList<String> all_background;
    ImageView back;
    ProgressBar progress;
    TextView txt_name;
    TextView img_toolbar_crcname;
    ImageView img_top_cal, img_top_faq;
    TextView txt_explorenam, txt_chat_name, txt_collctnz_nam, txt_accnt_name;
    RecyclerAdapterCategory recyclerAdapterCategory;
    ImageView img_country;
    LinearLayout lin_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);


        toolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View item = toolbar.findViewById(R.id.img_top_faq);
                if (item != null) {
                    toolbar.removeOnLayoutChangeListener(this);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ObjectAnimator animator = ObjectAnimator
                                    .ofFloat(v, "rotation", v.getRotation() + 360);
                            animator.start();
                        }
                    });
                }
            }
        });
        txt_name = (TextView) findViewById(R.id.name);
        img_country = (ImageView) findViewById(R.id.lin_country);
        lin_recycler = (LinearLayout) findViewById(R.id.lin_recycler);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

//
        String fullname = preferences.getString("fullname", null);
        if (fullname != null) {
            txt_name.setText("Welcome " + fullname + ",");
        }
        back = (ImageView) findViewById(R.id.img_crcdtlnam);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }
        });
//img_top_cal= (ImageView) findViewById(R.id.img_top_calendar);
        img_toolbar_crcname = (TextView) findViewById(R.id.toolbar_CRCNAM);
        img_top_faq = (ImageView) findViewById(R.id.img_top_faq);
        txt_accnt_name = (TextView) findViewById(R.id.txt_accntname);
        txt_chat_name = (TextView) findViewById(R.id.txt_chatname);
        txt_collctnz_nam = (TextView) findViewById(R.id.txt_collectnsname);
        txt_explorenam = (TextView) findViewById(R.id.txt_explorename);
        img_account = (ImageView) findViewById(R.id.img_account);
        img_explore = (ImageView) findViewById(R.id.img_explore);
        img_chat = (ImageView) findViewById(R.id.img_chat);
        img_collections = (ImageView) findViewById(R.id.img_collections);
        lay_collections = (LinearLayout) findViewById(R.id.lay_collections);
        lay_collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_collections.performClick();

            }
        });
        lay_explore = (LinearLayout) findViewById(R.id.lay_explore);
        lay_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_explore.performClick();


            }
        });
        lay_chat = (LinearLayout) findViewById(R.id.lay_chat);
        lay_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_chat.performClick();

            }
        });
        lay_account = (LinearLayout) findViewById(R.id.lay_account);
        lay_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_account.performClick();


            }
        });


        img_account.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator
                        .ofFloat(v, "rotation", v.getRotation() + 360);
                animator.start();
                img_account.setBackgroundResource(R.drawable.account);
                txt_accnt_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));



                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            img_account.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                        }
                    }
                };

                img_account.animate().rotationBy(360).withEndAction(runnable).setDuration(1000).setInterpolator(new LinearInterpolator()).cancel();


            }
        });
        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator
                        .ofFloat(v, "rotation", v.getRotation() + 360);
                animator.start();
                img_chat.setBackgroundResource(R.drawable.chat);
                txt_chat_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));

            }
        });
        img_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator
                        .ofFloat(v, "rotation", v.getRotation() + 360);
                animator.start();
                img_explore.setBackgroundResource(R.drawable.explore);
                txt_explorenam.setTextColor(getResources().getColor(R.color.txtcolor_icons));

            }
        });
        img_collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator
                        .ofFloat(v, "rotation", v.getRotation() + 360);
                animator.start();
                img_collections.setBackgroundResource(R.drawable.collections);
                txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));


            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_vertical);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        country();
        progress.setVisibility(View.VISIBLE);

        recycler_discount_inflate();


    }

    private void country() {
        dealsModelArrayList = new ArrayList<>();
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = Constants.URL+"country.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest
                    (Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++  alldeals:" + response);


                            try {
                                JSONArray jsonarray = new JSONArray(response);


                                for (int i = 0; i < jsonarray.length(); i++) {


                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String imageid = jsonobject.getString("id");
                                    String country = jsonobject.getString("country");
                                    image = jsonobject.getString("image");
                                    Glide
                                            .with(getApplicationContext())
                                            .load(image)
                                            .centerCrop()


                                            .crossFade()
                                            .into(img_country);
                                //    Picasso.with(getApplicationContext()).load(image).fit().into(img_country);


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //tv.setText("That didn't work!");

                        }
                    });
// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this,SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("")
                    .setContentText("Oops Your Connection Seems Off..")

                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));



        }

    }


    private void recycler_discount_inflate() {

        categoryModelArrayList = new ArrayList<>();
        dealsModelArrayList = new ArrayList<>();
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            String url = Constants.URL+"alldeals.php";

            StringRequest stringRequest1 = new StringRequest
                    (Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++  alldeals:" + response);


                            try {
                                JSONArray jsonarray = new JSONArray(response);


                                for (int i = 0; i < jsonarray.length(); i++) {
                                    DealsModel dealsModel = new DealsModel();

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String id1 = jsonobject.getString("id");
                                    title = jsonobject.getString("title");
                                    image = jsonobject.getString("image");
                                    description = jsonobject.getString("description");
                                    discount = jsonobject.getString("discount");
                                    timing = jsonobject.getString("timing");
                                    delivery = jsonobject.getString("delivery");
                                    category1 = jsonobject.getString("category");
                                    tags = jsonobject.getString("tags");
                                    seller_id = jsonobject.getString("seller_id");
                                    dealsModel.setId(id1);
                                    dealsModel.setCategory(category);
                                    dealsModel.setTitle(title);
                                    dealsModel.setDelivery(delivery);
                                    dealsModel.setDescription(description);
                                    dealsModel.setDiscount(discount);
                                    dealsModel.setImage(image);
                                    dealsModel.setTags(tags);
                                    dealsModel.setSeller_id(seller_id);
                                    dealsModel.setTiming(timing);
                                    dealsModel.setType("deal");
                                    dealsModelArrayList.add(dealsModel);


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //tv.setText("That didn't work!");

                        }
                    });



            String url1 = Constants.URL+"categories.php";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest
                    (Request.Method.POST, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   categories :" + response);


                            try {
                                JSONArray jsonarray = new JSONArray(response);

                                all_background = new ArrayList<>();

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    CategoryModel categoryModel = new CategoryModel();

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String id = jsonobject.getString("id");
                                    String category = jsonobject.getString("category");
                                    background = jsonobject.getString("background");
                                    icon = jsonobject.getString("icon");

                                    categoryModel.setBackground(background);
                                    categoryModel.setCategory(category);
                                    categoryModel.setIcon(icon);
                                    categoryModel.setId(id);
                                    categoryModel.setType("category");
                                    categoryModelArrayList.add(categoryModel);
                                    all_background.add(background);

                                }


                                if (!categoryModelArrayList.isEmpty() && !dealsModelArrayList.isEmpty() && categoryModelArrayList != null && dealsModelArrayList != null) {

                                    recyclerAdapterCategory = new RecyclerAdapterCategory(categoryModelArrayList, dealsModelArrayList, MainActivity.this);


                                    recyclerView.scheduleLayoutAnimation();
                                    recyclerView.setAdapter(recyclerAdapterCategory);
                                    progress.setVisibility(View.INVISIBLE);




                                    System.out.println("dealsisze" + dealsModelArrayList.size());
                                    System.out.println("categrysize" + categoryModelArrayList.size());
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //tv.setText("That didn't work!");

                        }
                    });

            queue.add(stringRequest1);
            queue.add(stringRequest);
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this,SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("")
                    .setContentText("Oops Your Connection Seems Off..")

                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {


            super.onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbr_cart, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_cart));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_top_faq.setVisibility(View.INVISIBLE);
                //img_top_cal.setVisibility(View.INVISIBLE);
                img_toolbar_crcname.setVisibility(View.GONE);
                back.setVisibility(View.GONE);

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                img_top_faq.setVisibility(View.VISIBLE);
                // img_top_cal.setVisibility(View.VISIBLE);
                img_toolbar_crcname.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                return false;
            }
        });

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(R.string.search_hint));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                recyclerAdapterCategory.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                recyclerAdapterCategory.filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
