# KKRefreshLayout

The KKRefreshLayout should be used whenever the user can refresh the contents of a view via a vertical/horizontal swipe gesture.
And support custom design header and footer view.

## Set-up

### Download
Download [the latest aar](https://github.com/0kai/KKRefreshLayout/releases) or grab via Gradle:
```groovy
compile 'net.z0kai:kkrefreshlayout:0.1.1'
```
or Maven:
```xml
<dependency>
  <groupId>net.z0kai</groupId>
  <artifactId>kkrefreshlayout</artifactId>
  <version>0.1.1</version>
  <type>pom</type>
</dependency>
```

### Layout
```xml
<net.z0kai.kkrefreshlayout.KKRefreshLayout
    android:id="@+id/refreshLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:rlLoadMoreEnable="true">
    // one child view
</net.z0kai.kkrefreshlayout.KKRefreshLayout>
```

### Listener
```java
refreshLayout.setRefreshListener(new KKRefreshListener() {
    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
    }
});
```

### Customization
You can custom header/footer view for your app by implements [IHeaderView](library/src/main/java/net/z0kai/kkrefreshlayout/view/IHeaderView.java)/[IFooterView](library/src/main/java/net/z0kai/kkrefreshlayout/view/IFooterView.java).
And config for all refresh layout in your app, see [KKRefreshLayoutConfig](library/src/main/java/net/z0kai/kkrefreshlayout/KKRefreshLayoutConfig.java).
```java
// only for vertical
KKRefreshLayoutConfig.setHeaderViewProvider(yourProvider);
KKRefreshLayoutConfig.setFooterViewProvider(yourProvider);
```
or set header/footer view activity onCreate
```java
refreshLayout.setHeaderView(new EmptyHeaderView(this));
refreshLayout.setFooterView(new ArrowFooterView(this));
```

## Demo
> you can push your custom header/footer view into the demo app, and then add the class in the list of [AppConfigs.java](app/src/main/java/net/z0kai/kkrefreshlayout_demo/AppConfig.java)

![](static/default-refresh.gif) ![](static/custom-refresh.gif) ![](static/horizontal-refresh.gif)

## Author
Z0Kai, @0kai on GitHub, [blog-中文](http://www.0kai.net)

## License
MIT-licensed.
