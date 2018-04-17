package com.meridian.dateout.account;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meridian.dateout.R;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> _listDataHeader;

    private HashMap<String, String> _listDataChild;
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, String> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;


    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        WebView web_child= (WebView) convertView.findViewById(R.id.web_child);
        String   childTexts = childText.replace("\\u0092", "&apos;");
        System.out.println("childtextttt000..."+  childTexts);
        String txtinc = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + childText + "</div";
        web_child.loadUrl("javascript:document.body.style.color=\"#000000\";");
        web_child.setBackgroundColor(Color.TRANSPARENT);
        Resources res1 = _context.getResources();
        float fontSize;
        fontSize = res1.getDimension(R.dimen.textsize);
        web_child.getSettings().setJavaScriptEnabled(true);
        String scandinavianCharacters = "\u0093";
        txtinc.replaceAll("", "'");
        childText.replaceAll("", "'");
        System.out.println("txtinc..."+txtinc);
        System.out.println("childtextttt..."+  childText);
        web_child.loadDataWithBaseURL("", txtinc, "text/html", "UTF-8", "");
        Resources res3 = _context.getResources();
        WebSettings settings = web_child.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setTextZoom(80);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_layout, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.text_parent);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        TextView parent_sub_text = (TextView) convertView.findViewById(R.id.parent_sub_text);
        Pattern pattern = Pattern.compile(",");
        Matcher matcher = pattern.matcher(headerTitle);
        if (matcher.find()) {
          String  string1 = headerTitle.substring(0, matcher.start());
          String  string2 = headerTitle.substring(matcher.end());
            lblListHeader.setText(string1);
            parent_sub_text.setText(string2);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.img_down);
        if (isExpanded)
        {
            img.setImageResource(R.drawable.down);
        }
        else
        {
            img.setImageResource(R.drawable.up);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}