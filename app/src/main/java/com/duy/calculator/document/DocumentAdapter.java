/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.calculator.document;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.calculator.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Duy on 19-May-17.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private static final String DOC_PATH = "doc/functions/";
    private static final String TAG = "DocumentAdapter";
    private Context context;
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> originalData = new ArrayList<>();
    private LayoutInflater inflater;
    private OnDocumentClickListener onDocumentClickListener;

    public DocumentAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        loadData();
    }

    private void loadData() {
        try {
            String[] functions = context.getAssets().list("doc/functions");
            Collections.addAll(fileNames, functions);
            originalData.addAll(fileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String fileName = fileNames.get(position);
        if (fileName.toLowerCase().endsWith(".md")) {
            String functionName = fileName.substring(0, fileName.length() - 3);
            holder.txtName.setText(functionName);
            holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDocumentClickListener != null) {
                        onDocumentClickListener.onDocumentClick(DOC_PATH + fileName);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    public void query(String query) {
        fileNames.clear();
        notifyDataSetChanged();
        for (String s : originalData) {
            if (s.toLowerCase().contains(query.toLowerCase())) {
                fileNames.add(s);
                notifyItemInserted(fileNames.size() - 1);
            }
        }
    }

    public void setOnDocumentClickListener(OnDocumentClickListener onDocumentClickListener) {
        this.onDocumentClickListener = onDocumentClickListener;
    }

    public interface OnDocumentClickListener {
        void onDocumentClick(String path);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }
}
