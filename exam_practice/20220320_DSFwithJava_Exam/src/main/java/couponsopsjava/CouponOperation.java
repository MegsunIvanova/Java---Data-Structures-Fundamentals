package couponsopsjava;

import java.util.*;
import java.util.stream.Collectors;


public class CouponOperation implements ICouponOperation {
    private Map<Website, List<Coupon>> websites;
    private Map<String, Website> websitesByDomains;
    private Map<String, Coupon> couponsByCode;

    public CouponOperation() {
        websites = new HashMap<>();
        websitesByDomains = new HashMap<>();
        couponsByCode = new HashMap<>();
    }

    public void registerSite(Website w) {
        if (exist(w)) {
            throw new IllegalArgumentException();
        }

        websites.put(w, new ArrayList<>());
        websitesByDomains.put(w.domain, w);
    }

    public void addCoupon(Website w, Coupon c) {
        if (!exist(w) || exist(c)) {
            throw new IllegalArgumentException();
        }

        websites.get(w).add(c);
        couponsByCode.put(c.code, c);
        c.setWebsite(w.domain);

    }

    public Website removeWebsite(String domain) {
        Website websiteForRemoving = websitesByDomains.get(domain);

        if (websiteForRemoving == null) {
            throw new IllegalArgumentException();
        }

        websitesByDomains.remove(domain);

        List<Coupon> couponsForRemoving = websites.remove(websiteForRemoving);

        couponsForRemoving.stream()
                .map(Coupon::getCode)
                .forEach(couponsByCode::remove);

        return websiteForRemoving;
    }

    public Coupon removeCoupon(String code) {
        Coupon couponForRemoving = couponsByCode.get(code);

        if (couponForRemoving == null) {
            throw new IllegalArgumentException();
        }

        couponsByCode.remove(code);

        Website couponWebsite = websitesByDomains.get(couponForRemoving.getWebsite());

        websites.get(couponWebsite).remove(couponForRemoving);

        return couponForRemoving;
    }

    public boolean exist(Website w) {
        return websites.containsKey(w);
    }

    public boolean exist(Coupon c) {
        return couponsByCode.containsKey(c.code);
    }

    public Collection<Website> getSites() {
        return websites.keySet();
    }

    public Collection<Coupon> getCouponsForWebsite(Website w) {
        if (!exist(w)) {
            throw new IllegalArgumentException();
        }

        return websites.get(w);
    }

    public void useCoupon(Website w, Coupon c) {
        if (!exist(w) || !exist(c)) {
            throw new IllegalArgumentException();
        }

        boolean isRemoved = websites.get(w).remove(c);

        if (!isRemoved) {
            throw new IllegalArgumentException();
        }

        couponsByCode.remove(c.code);

    }

    public Collection<Coupon> getCouponsOrderedByValidityDescAndDiscountPercentageDesc() {
        return couponsByCode.values()
                .stream()
                .sorted(Comparator.comparingInt(Coupon::getValidity)
                        .thenComparingInt(Coupon::getDiscountPercentage).reversed())
                .collect(Collectors.toList());
    }

    public Collection<Website> getWebsitesOrderedByUserCountAndCouponsCountDesc() {
        return websitesByDomains.values().stream()
                .sorted(Comparator.comparingInt(Website::getUsersCount).reversed()
                        .thenComparingInt(w -> websites.get(w).size())
                        .reversed())
                .collect(Collectors.toList());
    }

}
