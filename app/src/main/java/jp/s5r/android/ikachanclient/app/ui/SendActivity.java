package jp.s5r.android.ikachanclient.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import jp.s5r.android.ikachanclient.App;
import jp.s5r.android.ikachanclient.R;
import jp.s5r.android.ikachanclient.model.Room;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SendActivity extends BaseActivity {

    @InjectView(R.id.send_room_edittext)
    EditText mRoomEditText;
    @InjectView(R.id.send_room_list)
    ListView mRoomList;
    @InjectView(R.id.send_message_edittext)
    EditText mMessageEditText;

    private Realm mDb;
    private RoomAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.inject(this);

        initDb();
        initActionBar();
        initViews();

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            mMessageEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    private void initDb() {
        if (mDb != null) {
            mDb.close();
        }
        mDb = Realm.getInstance(getApplicationContext());
    }

    private void initActionBar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.action_bar);
            ab.getCustomView().findViewById(R.id.action_bar_button_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ab.getCustomView().findViewById(R.id.action_bar_button_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSubmit(mRoomEditText.getText().toString(), mMessageEditText.getText().toString());
                }
            });
        }
    }

    private void initViews() {
        mRoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = mAdapter.getItem(position);
                mRoomEditText.setText(room.getName());
                mRoomEditText.clearFocus();
                mRoomList.setVisibility(View.GONE);
            }
        });
        mRoomList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Room room = mAdapter.getItem(position);
                mDb.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Room.class)
                                .equalTo("id", room.getId())
                                .findAll()
                                .clear();
                    }
                });
                return true;
            }
        });

        mRoomEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRoomList.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        mMessageEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mRoomList.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmResults<Room> rooms = mDb.where(Room.class).findAll();
        rooms.sort("lastUsedAt", false);
        mAdapter = new RoomAdapter(getApplicationContext(), rooms, true);
        mRoomList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void onSubmit(final String roomName, final String message) {
        if (TextUtils.isEmpty(roomName) || TextUtils.isEmpty(message)) {
            return;
        }
        if (mDb.where(Room.class).equalTo("name", roomName).count() == 0) {
            mDb.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Room room = realm.createObject(Room.class);
                    room.setId(realm.where(Room.class).count() + 1);
                    room.setName(roomName);
                    room.setLastUsedAt(new Date());
                }
            });
        }

        sendToIkachan(roomName, message);
    }

    public void sendToIkachan(final String roomName, final String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        App.getInstance().getIkachanApi().notice(roomName, message, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                mProgressDialog.dismiss();
                showSuccessMessage();
                SendActivity.this.finish();
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressDialog.dismiss();
                showFailedMessage(error);
                SendActivity.this.finish();
            }
        });
    }

    private void showSuccessMessage() {
        Toast.makeText(this, "Ikachan success", Toast.LENGTH_SHORT).show();
    }

    private void showFailedMessage(Throwable e) {
        if (e != null) {
            Toast.makeText(this, "Ikachan failed: " + e.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ikachan failed", Toast.LENGTH_SHORT).show();
        }
    }

    static class RoomAdapter extends RealmBaseAdapter<Room> {
        private final LayoutInflater mInflater;

        public RoomAdapter(Context context, RealmResults<Room> realmResults, boolean automaticUpdate) {
            super(context, realmResults, automaticUpdate);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Room room = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.select_room_item, null);
                holder = new ViewHolder();
                holder.text = (TextView)convertView.findViewById(R.id.select_room_list_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.text.setText(room.getName());
            return convertView;
        }

        static class ViewHolder {
            TextView text;
        }
    }
}
