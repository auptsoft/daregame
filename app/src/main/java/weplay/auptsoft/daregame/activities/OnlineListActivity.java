package weplay.auptsoft.daregame.activities;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import weplay.auptsoft.daregame.R;
import weplay.auptsoft.daregame.services.response.ServerUtil;

/**
 * Created by Administrator on 12/16/2018.
 */

public class OnlineListActivity extends AppCompatActivity {
    ListView listView;
    LinearLayout indicatorView;

    ImageView backImageView;
    EditText searchEdit;

    ArrayList<ListItem> arrayList;
    QueryArrayAdapter arrayAdapter;

    ArrayList<ListItem> currentList;

    String columnOne, columnTwo, columnOne_ret, columnTwo_ret;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        columnOne = getIntent().getStringExtra("table");
        columnOne = getIntent().getStringExtra("columnOne");
        columnTwo = getIntent().getStringExtra("columnTwo");
        columnOne_ret = getIntent().getStringExtra("columnOne_ret");
        columnTwo_ret = getIntent().getStringExtra("columnTwo_ret");


        currentList = new ArrayList<>();
        arrayList = new ArrayList<>();
        arrayAdapter = new QueryArrayAdapter(this, R.layout.list_item_layout, currentList);

        listView = (ListView)findViewById(R.id.list_activity_list_view_id);
        listView.setAdapter(arrayAdapter);
        indicatorView = (LinearLayout)findViewById(R.id.loader_indicator);

        backImageView = (ImageView)findViewById(R.id.list_back_id);
        searchEdit = (EditText)findViewById(R.id.list_search_edit_id);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);

                finishAfterTransition();
            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String inputString = textView.getText().toString();
                //Toast.makeText(getBaseContext(), inputString, Toast.LENGTH_LONG).show();

                currentList.clear();
                currentList.addAll(getMatched(inputString, arrayList));
                arrayAdapter.notifyDataSetChanged();

                return true;
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputString = editable.toString();
                currentList.clear();
                currentList.addAll(getMatched(inputString, arrayList));
                arrayAdapter.notifyDataSetChanged();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListItem listItem = arrayList.get(i);

                Intent intent = new Intent();
                intent.putExtra(columnOne_ret, listItem.getColumnOne_ret());
                intent.putExtra(columnTwo_ret, listItem.getColumnTwo_ret());

                setResult(RESULT_OK, intent);

                finishAfterTransition();
            }
        });

        initializeData();
        //initializeTestData();

    }

    void initializeData() {
            String urlString = getIntent().getStringExtra("url");
            ServerUtil.sentGetRequest(urlString, new ServerUtil.OnResultListener() {
                @Override
                public void onResult(String jsonString, HttpURLConnection httpURLConnection) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);
                        arrayList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ListItem item = new ListItem(jsonObject.getString(columnOne), jsonObject.getString(columnTwo));
                            item.setColumnOne_ret(jsonObject.getString(columnOne_ret));
                            item.setColumnTwo_ret(jsonObject.getString(columnTwo_ret));
                            arrayList.add(item);
                        }
                        currentList.clear();
                        currentList.addAll(arrayList);
                        arrayAdapter.notifyDataSetChanged();
                    } catch (JSONException je) {
                        Toast.makeText(getBaseContext(), je.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    indicatorView.setVisibility(View.GONE);
                }

                @Override
                public void onError(String errorString) {
                    Toast.makeText(getBaseContext(), errorString, Toast.LENGTH_LONG).show();
                    indicatorView.setVisibility(View.GONE);
                }
            });
    }

    void initializeTestData() {

        /*arrayList.add("Benin");
        arrayList.add("Lagos");
        arrayList.add("Port-Harcourt");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        indicatorView.setVisibility(View.GONE); */
    }

    ArrayList<ListItem> getMatched(String value, ArrayList<ListItem> itemArrayList) {
        ArrayList<ListItem> arrayList = new ArrayList<>();
        for(ListItem item  : itemArrayList) {
            if(item.getTitle().toLowerCase().contains(value.toLowerCase()) || item.getSubtitle().toLowerCase().contains(value.toLowerCase()) ) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }

    private class QueryArrayAdapter extends ArrayAdapter {
        View view;
        int layoutRes;
        ArrayList<ListItem> modelArrayList;
        public QueryArrayAdapter(Context context, int resource, ArrayList<ListItem> mList) {
            super(context, resource, mList);
            layoutRes = resource;
            modelArrayList = mList;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutRes, null);

            TextView parishNameView = (TextView)view.findViewById(R.id.list_item_title_id);
            TextView parishAddressView = (TextView)view.findViewById(R.id.list_item_subtitle_id);

            parishNameView.setText(modelArrayList.get(position).getTitle());
            parishAddressView.setText(modelArrayList.get(position).getSubtitle());

            return view;
        }
    }

    class ListItem {
        private String title;
        private String subtitle;
        private String columnOne_ret = "";
        private String columnTwo_ret = "";

        public ListItem(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getColumnOne_ret() {
            return columnOne_ret;
        }

        public void setColumnOne_ret(String columnOne_ret) {
            this.columnOne_ret = columnOne_ret;
        }

        public String getColumnTwo_ret() {
            return columnTwo_ret;
        }

        public void setColumnTwo_ret(String columnTwo_ret) {
            this.columnTwo_ret = columnTwo_ret;
        }
    }
}
