package com.mooo.ewolvy.chipmental.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mooo.ewolvy.chipmental.R;
import com.mooo.ewolvy.chipmental.model.ListItem;
import java.util.List;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipHolder>{

    private List<ListItem> listData;
    private LayoutInflater inflater;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback{
        void onItemClick(View v, int p);
    }

    public void setItemClickCallback (final ItemClickCallback itemClickCallback){
        this.itemClickCallback = itemClickCallback;
    }

    public ChipAdapter(List<ListItem> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public ChipHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ChipHolder(view);
    }

    @Override
    public void onBindViewHolder(ChipHolder holder, int position) {
        ListItem item = listData.get(position);
        holder.limiting.setText(item.getLimiting());
        holder.growing.setText(item.getGrowing());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ChipHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView limiting;
        private TextView growing;
        private View container;

        ChipHolder(View itemView) {
            super(itemView);

            limiting = (TextView) itemView.findViewById(R.id.txt_limitating);
            growing = (TextView) itemView.findViewById(R.id.txt_growing);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(v, getAdapterPosition());
        }
    }
}
