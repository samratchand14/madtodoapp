package com.schand.madtodo.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.schand.madtodo.R;
import com.schand.madtodo.models.Todo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoHolder> {

    private List<Todo> todos = new ArrayList<>();
    private OnItemClickListener listener;
    private TodoViewModel vm;

    public TodoListAdapter(TodoViewModel todoViewModel) {

        this.vm = todoViewModel;

    }


    @NonNull
    @Override
    public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new TodoHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TodoHolder holder, int position) {

        final Todo currentTodo = todos.get(position);
        if(currentTodo.isIs_completed()){
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            holder.textViewTitle.setPaintFlags(0);
        }
        holder.textViewTitle.setText(currentTodo.getTitle());
        holder.textViewDescription.setText(currentTodo.getDescription());
        int priorityNum = currentTodo.getPriority();

        //change icons in the imageview
        if(priorityNum == Todo.PRIORITY_HIGH){
            holder.imageViewPriority.setImageResource(R.drawable.icon_important_red); }
        else if(priorityNum == Todo.PRIORITY_MEDIUM){
            holder.imageViewPriority.setImageResource(R.drawable.icon_important_orange);
        }else{
            holder.imageViewPriority.setImageResource(R.drawable.icon_important_green);
        }

        holder.checkTodo.setOnCheckedChangeListener(null);
        //sets date
        holder.textViewDate.setText(currentTodo.getDuedate());
        holder.checkTodo.setChecked(currentTodo.isIs_completed());

        holder.checkTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentTodo.setIs_completed(isChecked);
                Log.d("checked",currentTodo.toString());
                vm.update(currentTodo);
            }
        });
    }

    /**Gets text value for priority
     *
     * @param priorityNum integer value of priority
     * @return text value of priority
     */
    private CharSequence getTextForPriority(int priorityNum) {
        switch (priorityNum){
            case Todo.PRIORITY_HIGH:
                return "high";
            case Todo.PRIORITY_MEDIUM:
                return "medium";
            case Todo.PRIORITY_LOW:
                return "low";
            default:
                return "high";
        }
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    /**Sets the list of todoitems and notify observers
     *
     * @param todos List of todoitems
     */
    public void setTodos(List<Todo> todos) {

        this.todos = todos;
        notifyDataSetChanged();
    }

    /**Get todoitem at specified position
     *
     * @param position
     * @return
     */
    public Todo getTodoAt(int position) {
        return todos.get(position);
    }

    class TodoHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private ImageView imageViewPriority;
        private TextView textViewDate;
        private CheckBox checkTodo;

        public TodoHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            imageViewPriority = itemView.findViewById(R.id.image_view_priority);
            textViewDate=itemView.findViewById(R.id.text_view_date);
            checkTodo = itemView.findViewById(R.id.task_check);

            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(todos.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Todo todo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
