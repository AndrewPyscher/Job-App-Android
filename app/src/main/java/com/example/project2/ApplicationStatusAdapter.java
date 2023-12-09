package com.example.project2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ApplicationStatusAdapter extends RecyclerView.Adapter<ApplicationStatusAdapter.ViewHolder> {

    Context context;
    ArrayList<AppStatusObj> jobApps = new ArrayList<>();

    public ApplicationStatusAdapter(Context context, ArrayList<AppStatusObj> jobApps) {
        this.context = context;
        this.jobApps = jobApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_application_status, parent, false);
        Log.d("test","view holder created");
        return new ApplicationStatusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppStatusObj appStatus = jobApps.get(position);

        holder.tvEmployerName.setText(appStatus.companyName);
        holder.tvPosition.setText(appStatus.position);
        holder.tvStatus.setText(appStatus.status);
    }

    @Override
    public int getItemCount() {
        return jobApps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmployerName, tvPosition, tvStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvEmployerName = itemView.findViewById(R.id.tvEmployerName);

        }
    }
}
