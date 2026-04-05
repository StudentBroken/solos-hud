package android.support.wearable.view.drawer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.R;
import android.support.wearable.view.ResourcesUtil;
import android.support.wearable.view.drawer.WearableActionDrawerMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Objects;

/* JADX INFO: loaded from: classes33.dex */
@TargetApi(23)
public class WearableActionDrawer extends WearableDrawerView {
    private static final String TAG = "WearableActionDrawer";
    private final RecyclerView mActionList;
    private final RecyclerView.Adapter<RecyclerView.ViewHolder> mActionListAdapter;
    private final int mBottomPadding;
    private final int mFirstItemTopPadding;
    private final int mIconRightMargin;
    private final int mLastItemBottomPadding;
    private final int mLeftPadding;
    private Menu mMenu;
    private OnMenuItemClickListener mOnMenuItemClickListener;

    @Nullable
    private final ImageView mPeekActionIcon;

    @Nullable
    private final ImageView mPeekExpandIcon;
    private final int mRightPadding;
    private final boolean mShowOverflowInPeek;

    @Nullable
    private CharSequence mTitle;
    private final int mTopPadding;

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public WearableActionDrawer(Context context) {
        this(context, null);
    }

    public WearableActionDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableActionDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, null, null);
    }

    @VisibleForTesting
    WearableActionDrawer(Context context, AttributeSet attrs, int defStyleAttr, @Nullable ImageView peekActionIcon, @Nullable ImageView peekExpandIcon) {
        super(context, attrs, defStyleAttr);
        setShouldLockWhenNotOpenOrPeeking(true);
        boolean showOverflowInPeek = false;
        int menuRes = 0;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WearableActionDrawer, defStyleAttr, 0);
            try {
                this.mTitle = typedArray.getString(R.styleable.WearableActionDrawer_drawer_title);
                showOverflowInPeek = typedArray.getBoolean(R.styleable.WearableActionDrawer_show_overflow_in_peek, false);
                menuRes = typedArray.getResourceId(R.styleable.WearableActionDrawer_action_menu, 0);
            } finally {
                typedArray.recycle();
            }
        }
        this.mShowOverflowInPeek = showOverflowInPeek;
        if (peekActionIcon != null) {
            Log.w(TAG, "Using injected peek and action icons. Should only occur in tests.");
            this.mPeekActionIcon = peekActionIcon;
            this.mPeekExpandIcon = peekExpandIcon;
        } else if (!this.mShowOverflowInPeek) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View peekView = layoutInflater.inflate(R.layout.action_drawer_peek_view, getPeekContainer(), false);
            setPeekContent(peekView);
            this.mPeekActionIcon = (ImageView) peekView.findViewById(R.id.wearable_support_action_drawer_peek_action_icon);
            this.mPeekExpandIcon = (ImageView) peekView.findViewById(R.id.wearable_support_action_drawer_expand_icon);
        } else {
            this.mPeekActionIcon = null;
            this.mPeekExpandIcon = null;
        }
        if (menuRes != 0) {
            MenuInflater inflater = new MenuInflater(context);
            inflater.inflate(menuRes, getMenu());
        }
        int screenWidthPx = ResourcesUtil.getScreenWidthPx(context);
        int screenHeightPx = ResourcesUtil.getScreenHeightPx(context);
        Resources res = getResources();
        this.mTopPadding = res.getDimensionPixelOffset(R.dimen.action_drawer_item_top_padding);
        this.mBottomPadding = res.getDimensionPixelOffset(R.dimen.action_drawer_item_bottom_padding);
        this.mLeftPadding = ResourcesUtil.getFractionOfScreenPx(context, screenWidthPx, R.fraction.action_drawer_item_left_padding);
        this.mRightPadding = ResourcesUtil.getFractionOfScreenPx(context, screenWidthPx, R.fraction.action_drawer_item_right_padding);
        this.mFirstItemTopPadding = ResourcesUtil.getFractionOfScreenPx(context, screenHeightPx, R.fraction.action_drawer_item_first_item_top_padding);
        this.mLastItemBottomPadding = ResourcesUtil.getFractionOfScreenPx(context, screenHeightPx, R.fraction.action_drawer_item_last_item_bottom_padding);
        this.mIconRightMargin = res.getDimensionPixelOffset(R.dimen.action_drawer_item_icon_right_margin);
        this.mActionList = new RecyclerView(context);
        this.mActionList.setLayoutManager(new LinearLayoutManager(context));
        this.mActionListAdapter = new ActionListAdapter(getMenu());
        this.mActionList.setAdapter(this.mActionListAdapter);
        setDrawerContent(this.mActionList);
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        return isOpened();
    }

    @Override // android.support.wearable.view.drawer.WearableDrawerView
    public void onPeekContainerClicked(View v) {
        if (this.mShowOverflowInPeek) {
            super.onPeekContainerClicked(v);
        } else {
            onMenuItemClicked(0);
        }
    }

    @Override // android.support.wearable.view.drawer.WearableDrawerView
    int preferGravity() {
        return 80;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setTitle(@Nullable CharSequence title) {
        if (!Objects.equals(title, this.mTitle)) {
            CharSequence oldTitle = this.mTitle;
            this.mTitle = title;
            if (oldTitle == null) {
                this.mActionListAdapter.notifyItemInserted(0);
            } else if (title == null) {
                this.mActionListAdapter.notifyItemRemoved(0);
            } else {
                this.mActionListAdapter.notifyItemChanged(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasTitle() {
        return this.mTitle != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMenuItemClicked(int position) {
        if (position >= 0 && position < getMenu().size()) {
            WearableActionDrawerMenu.WearableActionDrawerMenuItem menuItem = (WearableActionDrawerMenu.WearableActionDrawerMenuItem) getMenu().getItem(position);
            if (!menuItem.invoke() && this.mOnMenuItemClickListener != null) {
                this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePeekIcons() {
        if (this.mPeekActionIcon != null && this.mPeekExpandIcon != null) {
            Menu menu = getMenu();
            int numberOfActions = menu.size();
            if (numberOfActions > 1) {
                setDrawerContent(this.mActionList);
                this.mPeekExpandIcon.setVisibility(0);
            } else {
                setDrawerContent(null);
                this.mPeekExpandIcon.setVisibility(8);
            }
            if (numberOfActions >= 1) {
                Drawable firstActionDrawable = menu.getItem(0).getIcon();
                if (firstActionDrawable != null) {
                    firstActionDrawable = firstActionDrawable.getConstantState().newDrawable().mutate();
                    firstActionDrawable.clearColorFilter();
                }
                this.mPeekActionIcon.setImageDrawable(firstActionDrawable);
                this.mPeekActionIcon.setContentDescription(menu.getItem(0).getTitle());
            }
        }
    }

    private final class ActionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int TYPE_ACTION = 0;
        public static final int TYPE_TITLE = 1;
        private final Menu mActionMenu;
        private final View.OnClickListener mItemClickListener = new View.OnClickListener() { // from class: android.support.wearable.view.drawer.WearableActionDrawer.ActionListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                int childPos = WearableActionDrawer.this.mActionList.getChildAdapterPosition(v) - (WearableActionDrawer.this.hasTitle() ? 1 : 0);
                if (childPos != -1) {
                    WearableActionDrawer.this.onMenuItemClicked(childPos);
                } else {
                    Log.w(WearableActionDrawer.TAG, "invalid child position");
                }
            }
        };

        public ActionListAdapter(Menu menu) {
            this.mActionMenu = WearableActionDrawer.this.getMenu();
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return (WearableActionDrawer.this.hasTitle() ? 1 : 0) + this.mActionMenu.size();
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            int titleAwarePosition = WearableActionDrawer.this.hasTitle() ? position - 1 : position;
            if (viewHolder instanceof ActionItemViewHolder) {
                ActionItemViewHolder holder = (ActionItemViewHolder) viewHolder;
                holder.view.setPadding(WearableActionDrawer.this.mLeftPadding, position == 0 ? WearableActionDrawer.this.mFirstItemTopPadding : WearableActionDrawer.this.mTopPadding, WearableActionDrawer.this.mRightPadding, position == getItemCount() + (-1) ? WearableActionDrawer.this.mLastItemBottomPadding : WearableActionDrawer.this.mBottomPadding);
                Drawable icon = this.mActionMenu.getItem(titleAwarePosition).getIcon();
                if (icon != null) {
                    icon = icon.getConstantState().newDrawable().mutate();
                }
                CharSequence title = this.mActionMenu.getItem(titleAwarePosition).getTitle();
                holder.textView.setText(title);
                holder.textView.setContentDescription(title);
                holder.iconView.setContentDescription(title);
                holder.iconView.setImageDrawable(icon);
                return;
            }
            if (viewHolder instanceof TitleViewHolder) {
                TitleViewHolder holder2 = (TitleViewHolder) viewHolder;
                holder2.view.setPadding(WearableActionDrawer.this.mLeftPadding, WearableActionDrawer.this.mFirstItemTopPadding, WearableActionDrawer.this.mRightPadding, WearableActionDrawer.this.mBottomPadding);
                holder2.textView.setText(WearableActionDrawer.this.mTitle);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 1:
                    View titleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_drawer_title_view, parent, false);
                    return new TitleViewHolder(titleView);
                default:
                    View actionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_drawer_item_view, parent, false);
                    actionView.setOnClickListener(this.mItemClickListener);
                    return new ActionItemViewHolder(WearableActionDrawer.this, actionView);
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return (WearableActionDrawer.this.hasTitle() && position == 0) ? 1 : 0;
        }
    }

    private final class ActionItemViewHolder extends RecyclerView.ViewHolder {
        public final ImageView iconView;
        public final TextView textView;
        public final View view;

        public ActionItemViewHolder(WearableActionDrawer wearableActionDrawer, View view) {
            super(view);
            this.view = view;
            this.iconView = (ImageView) view.findViewById(R.id.wearable_support_action_drawer_item_icon);
            ((LinearLayout.LayoutParams) this.iconView.getLayoutParams()).setMarginEnd(wearableActionDrawer.mIconRightMargin);
            this.textView = (TextView) view.findViewById(R.id.wearable_support_action_drawer_item_text);
        }
    }

    private static final class TitleViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final View view;

        public TitleViewHolder(View view) {
            super(view);
            this.view = view;
            this.textView = (TextView) view.findViewById(R.id.wearable_support_action_drawer_title);
        }
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            this.mMenu = new WearableActionDrawerMenu(getContext(), new WearableActionDrawerMenu.WearableActionDrawerMenuListener() { // from class: android.support.wearable.view.drawer.WearableActionDrawer.1
                @Override // android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuListener
                public void menuItemChanged(int position) {
                    if (WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyItemChanged(position);
                    }
                    if (position == 0) {
                        WearableActionDrawer.this.updatePeekIcons();
                    }
                }

                @Override // android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuListener
                public void menuItemAdded(int position) {
                    if (WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyItemChanged(position);
                    }
                    if (position <= 1) {
                        WearableActionDrawer.this.updatePeekIcons();
                    }
                }

                @Override // android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuListener
                public void menuItemRemoved(int position) {
                    if (WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyItemChanged(position);
                    }
                    if (position <= 1) {
                        WearableActionDrawer.this.updatePeekIcons();
                    }
                }

                @Override // android.support.wearable.view.drawer.WearableActionDrawerMenu.WearableActionDrawerMenuListener
                public void menuChanged() {
                    if (WearableActionDrawer.this.mActionListAdapter != null) {
                        WearableActionDrawer.this.mActionListAdapter.notifyDataSetChanged();
                    }
                    WearableActionDrawer.this.updatePeekIcons();
                }
            });
        }
        return this.mMenu;
    }
}
