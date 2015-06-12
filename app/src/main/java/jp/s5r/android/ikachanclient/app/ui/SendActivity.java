package jp.s5r.android.ikachanclient.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import jp.s5r.android.ikachanclient.R;
import jp.s5r.android.ikachanclient.model.Room;

public class SendActivity extends BaseActivity {

    @InjectView(R.id.send_room_edittext)
    EditText mSelectRoomEditText;
    @InjectView(R.id.send_room_list)
    ListView mSelectRoomList;

    private Realm mDb;
    private RoomAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

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
                    onSubmit(mSelectRoomEditText.getText().toString());
                }
            });
        }

        if (mDb != null) {
            mDb.close();
        }
        mDb = Realm.getInstance(getApplicationContext());

        ButterKnife.inject(this);
        mSelectRoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Room room = mAdapter.getItem(position);
                mSelectRoomEditText.setText(room.getName());
            }
        });
        mSelectRoomList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        mSelectRoomEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    onSubmit(mSelectRoomEditText.getText().toString());
                    return true;
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
        mSelectRoomList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void onSubmit(final String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        if (mDb.where(Room.class).equalTo("name", name).count() == 0) {
            mDb.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Room room = realm.createObject(Room.class);
                    room.setId(realm.where(Room.class).count() + 1);
                    room.setName(name);
                    room.setLastUsedAt(new Date());
                }
            });
        }
    }

    static class RoomAdapter extends RealmBaseAdapter<Room> {
        private final LayoutInflater mInflater;

        public RoomAdapter(Context context, RealmResults<Room> realmResults, boolean automaticUpdate) {
            super(context, realmResults, automaticUpdate);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Room room = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.select_room_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.select_room_list_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(room.getName());
            return convertView;
        }

        static class ViewHolder {
            TextView text;
        }
    }
}
