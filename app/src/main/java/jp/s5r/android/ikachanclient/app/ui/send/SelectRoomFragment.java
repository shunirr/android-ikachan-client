package jp.s5r.android.ikachanclient.app.ui.send;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import jp.s5r.android.ikachanclient.app.ui.BaseFragment;
import jp.s5r.android.ikachanclient.model.Room;

public class SelectRoomFragment extends BaseFragment {

    @InjectView(R.id.select_room_edittext)
    EditText mSelectRoomEditText;
    @InjectView(R.id.select_room_list)
    ListView mSelectRoomList;

    private Realm mDb;
    private RoomAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_room, null);
        ButterKnife.inject(this, v);
        mSelectRoomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        return v;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            if (mDb != null) {
                mDb.close();
            }
            mDb = Realm.getInstance(getActivity().getApplicationContext());
        }
    }

    @Override
    public void onDetach() {
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        RealmResults<Room> rooms = mDb.where(Room.class).findAll();
        rooms.sort("lastUsedAt", false);
        mAdapter = new RoomAdapter(getActivity().getApplicationContext(), rooms, true);
        mSelectRoomList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void onSubmit(final String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
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

    static class RoomAdapter extends RealmBaseAdapter<Room> {
        private final LayoutInflater mInflater;

        public RoomAdapter(Context context, RealmResults<Room> realmResults, boolean automaticUpdate) {
            super(context, realmResults, automaticUpdate);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Room room = getItem(position);
            View v = mInflater.inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) v.findViewById(android.R.id.text1);
            tv.setText(room.getName());
            return v;
        }
    }
}
