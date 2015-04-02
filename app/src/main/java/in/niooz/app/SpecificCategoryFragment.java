package in.niooz.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;


public class SpecificCategoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    private TextView fragtv;

    public static SpecificCategoryFragment newInstance(int position) {
        SpecificCategoryFragment f = new SpecificCategoryFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_specific_category_fragment,null);

        fragtv = (TextView) v.findViewById(R.id.fragtv);
        fragtv.setText("Position : " + position);
        return v;
    }
}
