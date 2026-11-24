package ru.oksei.coworking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.oksei.coworking.BookingActivity;
import ru.oksei.coworking.R;
import ru.oksei.coworking.models.Workspace;

public class WorkspaceAdapter extends RecyclerView.Adapter<WorkspaceAdapter.WorkspaceViewHolder> {

    private final Context context;
    private List<Workspace> workspaceList;

    public WorkspaceAdapter(Context context, List<Workspace> workspaceList) {
        this.context = context;
        this.workspaceList = workspaceList;
    }

    public void updateList(List<Workspace> newList) {
        this.workspaceList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkspaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workspace, parent, false);
        return new WorkspaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceViewHolder holder, int position) {
        Workspace ws = workspaceList.get(position);

        holder.tvName.setText(ws.getName());
        holder.tvCategory.setText(ws.getCategory());
        holder.tvCapacity.setText(ws.getCapacity() + " чел.");
        holder.tvPrice.setText(String.format("%.0f ₽ / час", ws.getPricePerHour()));
        holder.tvDescription.setText(ws.getDescription());
        holder.tvAmenities.setText("Оснащение: " + ws.getAmenities());

        holder.btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("workspace", ws);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workspaceList != null ? workspaceList.size() : 0;
    }

    public static class WorkspaceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvCapacity, tvPrice, tvDescription, tvAmenities;
        Button btnBook;

        public WorkspaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvWorkspaceName);
            tvCategory = itemView.findViewById(R.id.tvWorkspaceCategory);
            tvCapacity = itemView.findViewById(R.id.tvWorkspaceCapacity);
            tvPrice = itemView.findViewById(R.id.tvWorkspacePrice);
            tvDescription = itemView.findViewById(R.id.tvWorkspaceDescription);
            tvAmenities = itemView.findViewById(R.id.tvWorkspaceAmenities);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}
