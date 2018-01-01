package firebase.smdeveloper.vvsn.todo_list.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import firebase.smdeveloper.vvsn.todo_list.Interface.ItemClickListener;
import firebase.smdeveloper.vvsn.todo_list.MainActivity;
import firebase.smdeveloper.vvsn.todo_list.Model.ToDo;
import firebase.smdeveloper.vvsn.todo_list.R;

/**
 * Created by sathish kumar on 29-11-2017.
 */

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
{

    ItemClickListener itemClickListener;
    TextView item_title,item_desc;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = (TextView)itemView.findViewById(R.id.item_title);
        item_desc = (TextView)itemView.findViewById(R.id.item_desc);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
     
    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

   private MainActivity mainActivity;
   private List<ToDo> toDoList;

    public ListItemAdapter(MainActivity mainActivity, List<ToDo> toDoList) {
        this.mainActivity = mainActivity;
        this.toDoList = toDoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mainActivity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ListItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        //set data for item
        holder.item_title.setText(toDoList.get(position).getTitle());
        holder.item_desc.setText(toDoList.get(position).getDesc());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //When user select item ,data will auto set for Edit Text View
                mainActivity.title.setText(toDoList.get(position).getTitle());
                mainActivity.desc.setText(toDoList.get(position).getDesc());

                mainActivity.isUpdate=true;//set flag is update = true
                mainActivity.idUpdate = toDoList.get(position).getId();

            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}