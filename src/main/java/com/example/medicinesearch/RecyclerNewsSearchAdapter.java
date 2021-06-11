package com.example.medicinesearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerNewsSearchAdapter extends RecyclerView.Adapter<RecyclerNewsSearchAdapter.ViewHolder> implements Filterable {
    private ArrayList<NewsRecyclerItem> mlist, filteredList;

    public RecyclerNewsSearchAdapter(ArrayList<NewsRecyclerItem> list) {
        mlist = list;
        this.filteredList = list;

        Log.v("Adapter생성자 호출","Adapter생성자");
    }

    public ArrayList<NewsRecyclerItem> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(ArrayList<NewsRecyclerItem> filteredList) {
        this.filteredList = filteredList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            //검색 창에 단어 입력하면 요출 되는 메서드
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.v("performFiltering", "performFiltering :"+  constraint);
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = mlist;
                } else {
                    ArrayList<NewsRecyclerItem> filteringList = new ArrayList<>();
                    for(NewsRecyclerItem name : mlist) {
                        if(name.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            //performFiltering 메서드에서 필터링한 값을 매개변수로 받아 호출되는 메서드
            //마지막에 notifyDataSetChanged(); 호출하여 필터된   결과를 recyclerView를 출력한다.
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.v("publishResults", "publishResults ");
                filteredList = (ArrayList<NewsRecyclerItem>)results.values;
                notifyDataSetChanged();
            }
        };
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public RecyclerNewsSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.v("onCreateViewHolder 호출","아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴 "+"viewType: "+Integer.toString(viewType));

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_news_recycler_item, parent, false);
        RecyclerNewsSearchAdapter.ViewHolder vh = new RecyclerNewsSearchAdapter.ViewHolder(view);

        return vh;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull RecyclerNewsSearchAdapter.ViewHolder holder, int position) {

        NewsRecyclerItem item = filteredList.get(position);


        holder.title.setText(item.getTitle().replaceAll("&quot","\""));
        holder.description.setText(item.getDescription().replaceAll("&quot","\""));
        if(item.getCategory() != null)
        {
            holder.category.setText(item.getCategory());
        }


        Log.v("onBindViewHolder 호출","position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 "+"position: "+Integer.toString(position));

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        Log.v("getItemCount","전체 데이터 갯수 리턴: "+Integer.toString(filteredList.size()));
        return filteredList.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView description;
        TextView category;

        ViewHolder(View itemView) {
            super(itemView) ;

            Log.v("ViewHolder 생성자","ViewHolder 생성자 호출");

            // 뷰 객체에 대한 참조. (hold strong reference)

            title = itemView.findViewById(R.id.titleText);
            description = itemView.findViewById(R.id.contentText);
            category = itemView.findViewById(R.id.categoryText);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Log.v("getAdapterPosition",Integer.toString(pos));
                    if(pos != RecyclerView.NO_POSITION)
                    {
                        if(mListener != null){
                            Log.v("itemView onClick",Integer.toString(pos));
                            mListener.onItemClick(v, pos);
                        }

                    }
                }
            });

        }
    }


    //Item클릭 리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }


    private RecyclerNewsSearchAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(RecyclerNewsSearchAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

}



