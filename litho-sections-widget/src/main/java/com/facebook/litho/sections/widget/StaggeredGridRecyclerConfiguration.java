/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.litho.sections.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.sections.SectionTree;
import com.facebook.litho.widget.Binder;
import com.facebook.litho.widget.RecyclerBinder;
import com.facebook.litho.widget.StaggeredGridLayoutInfo;
import javax.annotation.Nullable;

/**
 * A configuration object for {@link RecyclerCollectionComponent} that will create a {@link
 * android.support.v7.widget.StaggeredGridLayoutManager} for the {@link RecyclerView}.
 */
public class StaggeredGridRecyclerConfiguration<T extends SectionTree.Target & Binder<RecyclerView>>
    implements RecyclerConfiguration {
  private static final RecyclerBinderConfiguration RECYCLER_BINDER_CONFIGURATION =
      new RecyclerBinderConfiguration(4.0);

  private final int mNumSpans;
  private final int mOrientation;
  private final boolean mReverseLayout;
  private final int mGapStrategy;
  private final RecyclerBinderConfiguration mRecyclerBinderConfiguration;

  public static StaggeredGridRecyclerConfiguration createWithRecyclerBinderConfiguration(
      int numSpans, RecyclerBinderConfiguration recyclerBinderConfiguration) {
    return new StaggeredGridRecyclerConfiguration(
        numSpans,
        StaggeredGridLayoutManager.VERTICAL,
        false,
        StaggeredGridLayoutManager.GAP_HANDLING_NONE,
        recyclerBinderConfiguration);
  }

  public StaggeredGridRecyclerConfiguration(int numSpans) {
    this(numSpans, StaggeredGridLayoutManager.VERTICAL, false);
  }

  public StaggeredGridRecyclerConfiguration(int numSpans, int orientation, boolean reverseLayout) {
    this(numSpans, orientation, reverseLayout, RECYCLER_BINDER_CONFIGURATION);
  }

  public StaggeredGridRecyclerConfiguration(
      int numSpans,
      int orientation,
      boolean reverseLayout,
      RecyclerBinderConfiguration recyclerBinderConfiguration) {
    this(
        numSpans,
        orientation,
        reverseLayout,
        StaggeredGridLayoutManager.GAP_HANDLING_NONE,
        recyclerBinderConfiguration);
  }

  public StaggeredGridRecyclerConfiguration(
      int numSpans,
      int orientation,
      boolean reverseLayout,
      int gapStrategy,
      RecyclerBinderConfiguration recyclerBinderConfiguration) {
    mNumSpans = numSpans;
    mOrientation = orientation;
    mReverseLayout = reverseLayout;
    mGapStrategy = gapStrategy;
    mRecyclerBinderConfiguration =
        recyclerBinderConfiguration == null
            ? RECYCLER_BINDER_CONFIGURATION
            : recyclerBinderConfiguration;
  }

  @Override
  public T buildTarget(ComponentContext c) {
    final RecyclerBinder recyclerBinder =
        new RecyclerBinder.Builder()
            .rangeRatio((float) mRecyclerBinderConfiguration.getRangeRatio())
            .layoutInfo(
                new StaggeredGridLayoutInfo(mNumSpans, mOrientation, mReverseLayout, mGapStrategy))
            .layoutHandlerFactory(mRecyclerBinderConfiguration.getLayoutHandlerFactory())
            .wrapContent(mRecyclerBinderConfiguration.isWrapContent())
            .fillListViewport(mRecyclerBinderConfiguration.getFillListViewport())
            .fillListViewportHScrollOnly(
                mRecyclerBinderConfiguration.getFillListViewportHScrollOnly())
            .threadPoolForParallelFillViewportConfig(
                mRecyclerBinderConfiguration.getThreadPoolForParallelFillViewportConfig())
            .enableStableIds(mRecyclerBinderConfiguration.getEnableStableIds())
            .invalidStateLogParamsList(mRecyclerBinderConfiguration.getInvalidStateLogParamsList())
            .build(c);
    return (T)
        new SectionBinderTarget(
            recyclerBinder, mRecyclerBinderConfiguration.getUseAsyncMutations());
  }

  @Override
  public @Nullable SnapHelper getSnapHelper() {
    return null;
  }

  @Override
  public int getOrientation() {
    return mOrientation;
  }

  @Override
  public boolean isWrapContent() {
    return mRecyclerBinderConfiguration.isWrapContent();
  }
}
