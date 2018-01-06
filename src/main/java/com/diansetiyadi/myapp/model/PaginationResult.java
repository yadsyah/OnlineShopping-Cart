package com.diansetiyadi.myapp.model;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class PaginationResult<E> {

    private int totalRecords;
    private int currentPage;
    private List<E> list;
    private int maxResult;
    private int totalPages;
    private int maxNavigationPage;
    private List<Integer> navigationPages;

    public PaginationResult(Query query, int page, int maxResult, int maxNavigationPage) {
        final int pageIndex = page - 1 < 0 ? 0 : page - 1;

        int fromRecordIndex = pageIndex * maxResult;
        int maxRecordIndex = fromRecordIndex + maxResult;

        ScrollableResults resultsScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);

        List results = new ArrayList();

        boolean hasResult = resultsScroll.first();

        if (hasResult) {
            hasResult = resultsScroll.scroll(fromRecordIndex);

            if (hasResult) {
                do {
                    E record = (E) resultsScroll.get(0);
                    results.add(record);
                }
                while (resultsScroll.next() && resultsScroll.getRowNumber() >= fromRecordIndex && resultsScroll.getRowNumber() < maxRecordIndex);
            }
            resultsScroll.last();
        }
        this.totalRecords = resultsScroll.getRowNumber() + 1;
        this.currentPage = pageIndex + 1;

        this.list = results;
        this.maxResult = maxResult;

        this.totalPages = (this.totalRecords / this.maxResult) + 1;
        this.maxNavigationPage = maxNavigationPage;

        if (maxNavigationPage < totalPages) {
            this.maxNavigationPage = maxNavigationPage;
        }
        this.calcNavigationPages();
    }

    private void calcNavigationPages() {

        this.navigationPages = new ArrayList<Integer>();

        int current = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;

        int begin = current - this.maxNavigationPage / 2;
        int end = current + this.maxNavigationPage / 2;

        // First page
        navigationPages.add(1);
        if (begin > 2) {
            // For '...'
            navigationPages.add(-1);
        }

        for (int i = begin; i < end; i++) {
            if (i > 1 && i < this.totalPages) {
                navigationPages.add(i);
            }
        }

        if (end < this.totalPages - 2) {
            // For '...'
            navigationPages.add(-1);
        }
        // Last page.
        navigationPages.add(this.totalPages);
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<E> getList() {
        return list;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Integer> getNavigationPages() {
        return navigationPages;
    }
}
