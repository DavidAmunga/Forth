package com.labs.buttercell.forth.model;

import java.util.List;

/**
 * Created by amush on 22-Dec-17.
 */

public class FCMResponse {
    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;

    public FCMResponse() {
    }

    public FCMResponse(long multicast_id, int success, int failure, int canonical_ids, List<Result> results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.canonical_ids = canonical_ids;
        this.results = results;
    }
}
