package ru.skyeng.listening.Modules.Categories;

import android.support.v4.app.Fragment;


/**
 * ---------------------------------------------------
 * Created by Sermilion on 15/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class CategoriesFragment extends Fragment{ //extends MvpFragment<
//        MVPView>,
//        CategoriesPresenter>
//        implements MVPView>,
//        SwipeRefreshLayout.OnRefreshListener {
//
//    protected boolean isRefreshing;
//    @BindView(R.id.tag_group)
//    TagView tagGroup;
//
//    private List<Integer> selectedTags;
//
//    public void resetSelectedTags() {
//
//        selectedTags.clear();
//        initTagView(selectedTags);
//        AudioListActivity.categoriesSelected = false;
//    }
//
//    public void setSelectedTags(List<Integer> selectedTags) {
//        this.selectedTags = selectedTags;
//    }
//
//    public List<Integer> getSelectedTags() {
//        return selectedTags;
//    }
//
//    @Inject
//    public void setPresenter(@NonNull CategoriesPresenter presenter) {
//        super.setPresenter(presenter);
//    }
//
//    @Inject
//    void setModel(CategoriesModel model) {
//        presenter.setModel(model);
//    }
//
//    @Inject
//    void setRetrofitService(TagsService service){
//        ((CategoriesModel)presenter.getModel()).setRetrofitService(service);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//        ((SEApplication) getAppContext()).getCategoriesDiComponent().inject(this);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_category_list, container, false);
//    }
//
//    @NonNull
//    @Override
//    public CategoriesPresenter createPresenter() {
//        return presenter;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstance) {
//        super.onViewCreated(view, savedInstance);
//        ButterKnife.bind(this, view);
//        contentView.setOnRefreshListener(this);
//        if (selectedTags != null) {
//            initTagView(selectedTags);
//        } else {
//            selectedTags = new ArrayList<>();
//        }
//        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
//            @Override
//            public void onTagClick(Tag tag, int position) {
//                if (selectedTags.contains(position)) {
//                    selectedTags.remove(Integer.valueOf(position));
//                } else {
//                    selectedTags.add(position);
//                }
//                initTagView(selectedTags);
//            }
//        });
//
//        if ((presenter.getModel()).getItems() == null) {
//            loadData(false);
//        } else {
//            initTagView(selectedTags);
//        }
//    }
//
//
//    @Override
//    public void showLoading(boolean pullToRefresh) {
//        super.showLoading(pullToRefresh);
//        contentView.setRefreshing(pullToRefresh);
//    }
//
//
//    @Override
//    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
//        if (e != null)
//            return e.getMessage();
//        return getActivityContext().getString(R.string.unknown_error);
//    }
//
//    @Override
//    public void setData(List<AudioTag> data) {
//
//    }
//
//    @Override
//    public void loadData(boolean pullToRefresh) {
//        if (!pullToRefresh)
//            ((ActivityExtensions) getActivity()).showProgress();
//        presenter.loadData(pullToRefresh, new CategoriesRequestParams());
//    }
//
//
//    @Override
//    public void onRefresh() {
//        isRefreshing = true;
//        loadData(true);
//    }
//
//    @Override
//    public void onSubscribe(Disposable d) {
//
//    }
//
//    @Override
//    public void onNext(TagsData value) {
//        presenter.getModel().setData(value);
//        isRefreshing = false;
//        setData(value.getPrimaryData());
//        initTagView(selectedTags);
//    }
//
//    private void initTagView(List<Integer> selected) {
//
//        if (selected == null) {
//            selected = new ArrayList<>();
//        } else if (selected.size() > 0) {
//            AudioListActivity.categoriesSelected = true;
//        }
//        if (presenter.getData() == null || tagGroup == null) return;
//        tagGroup.removeAll();
//        for (int i = 0; i < presenter.getData().size(); i++) {
//            AudioTag aTag = presenter.getData().get(i);
//            Tag tag = new Tag(aTag.getTitle());
//            tag.radius = 6;
//            if (getActivityContext() != null) {
//                if (selected.contains(i)) {
//                    tag.background = ContextCompat.getDrawable(getActivityContext(), R.drawable.blue2_with_shadow);
//                    tag.tagTextColor = ContextCompat.getColor(getActivityContext(), R.color.colorWhite);
//                } else {
//                    tag.layoutColor = ContextCompat.getColor(getActivityContext(), R.color.colorBlue0);
//                    tag.tagTextColor = ContextCompat.getColor(getActivityContext(), R.color.colorAccent);
//                }
//            }
//            tag.tagTextSize = 16;
//            tagGroup.addTag(tag);
//        }
//    }
//
//    @Override
//    public void onError(Throwable e) {
//        isRefreshing = false;
//        showError(e.getCause(), isRefreshing);
//        contentView.setRefreshing(false);
//        e.printStackTrace();
//        setData(new ArrayList<>());
//    }
//
//    @Override
//    public void onComplete() {
//        if(getActivity()!=null) {
//            ((ActivityExtensions) getActivity()).hideProgress();
//            contentView.setRefreshing(isRefreshing);
//        }
//    }

}
