package jp.s5r.android.ikachanclient.app.ui.send;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.s5r.android.ikachanclient.R;
import jp.s5r.android.ikachanclient.app.ui.BaseFragment;

public class SelectRoomFragment extends BaseFragment {

    @InjectView(R.id.select_room_edittext)
    EditText mSelectRoomEditText;
    @InjectView(R.id.select_room_list)
    ListView mSelectRoomList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_room, null);
        ButterKnife.inject(this, v);
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
    }

    static class RoomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
