package three.com.materialdesignexample.Framgment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import three.com.materialdesignexample.Adapter.ScoreAdapter;
import three.com.materialdesignexample.CallBack;
import three.com.materialdesignexample.Models.Score;
import three.com.materialdesignexample.R;
import three.com.materialdesignexample.Util.HandleResponseUtil;
import three.com.materialdesignexample.Util.HttpUtil;

/**
 * Created by Administrator on 2015/10/21.
 */
public class ScoreFramgment extends Fragment {

    private LinearLayout emptyLayout ;
    private LinearLayout scoreLayout;
    private View h_v;
    private Button importBtn;
    private ListView scoreLv;
    private ArrayList<Score> scoreData= HandleResponseUtil.scores;
    private ProgressDialog progressDialog;
    private TextView allScore_tv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.score_framgment, null);
        emptyLayout= (LinearLayout) view.findViewById(R.id.empty_layout);
        importBtn= (Button) view.findViewById(R.id.import_btn);
        scoreLv= (ListView) view.findViewById(R.id.score_lv);
        scoreLayout= (LinearLayout) view.findViewById(R.id.score_layout);
        h_v=view.findViewById(R.id.h_v);
        allScore_tv= (TextView) view.findViewById(R.id.allScore_tv);

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.getScoreHtml(getActivity(), new CallBack() {
                    @Override
                    public void onStart() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showProgressDialog();
                            }
                        });

                    }

                    @Override
                    public void onFinsh(String response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                allScore_tv.setText(HandleResponseUtil.allScore);
                                initView();
                                closeProgressDialog();
                            }
                        });
                        Log.d("TAG", "handle score OK");
                    }
                });
            }
        });

        return view;
    }

    private void initView(){
        scoreLv.setAdapter(new ScoreAdapter(getActivity(),scoreData));
        scoreLv.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        scoreLayout.setVisibility(View.VISIBLE);
        h_v.setVisibility(View.VISIBLE);
    }

    public  void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private  void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
