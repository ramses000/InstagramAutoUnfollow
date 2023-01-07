package org.example;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.collection.*;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowersRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserFollowingRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserInfoRequest;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUnfollowRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<Long> followerList = new ArrayList<Long>();
        ArrayList<Long> followingList = new ArrayList<Long>();
        Scanner mysc = new Scanner(System.in);
        System.out.println("Please input your Instagram username: ");
        String userName = mysc.nextLine();
        System.out.println("Please input your Instagram password: ");
        String passWord = mysc.nextLine();
        Instagram4j instagram = Instagram4j.builder().username(userName).password(passWord).build();
        instagram.setup();
        instagram.login();
        InstagramSearchUsernameResult username = instagram.sendRequest(new InstagramSearchUsernameRequest("githubdummy"));

        InstagramGetUserFollowersResult followers = instagram.sendRequest((new
                InstagramGetUserFollowersRequest(username.getUser().getPk())));
        for(InstagramUserSummary user : followers.getUsers()){
            followerList.add(user.pk);
        }

        InstagramGetUserFollowersResult following = instagram.sendRequest((new
                InstagramGetUserFollowingRequest(username.getUser().getPk())));
        for(InstagramUserSummary user : following.getUsers()){
            followingList.add(user.pk);
        }

        Collection<Long> nonFollower = CollectionUtils.subtract(followingList, followerList);
        Object[] onlyFollowers = new Object[nonFollower.size()];
        onlyFollowers = nonFollower.toArray();




        String onlyFollowersUsernames[] = new String[onlyFollowers.length];
        for(int i = 0; i<onlyFollowers.length; i++){
            InstagramSearchUsernameResult result = instagram.sendRequest(new InstagramGetUserInfoRequest((Long) onlyFollowers[i]));
            onlyFollowersUsernames[i] = result.getUser().getUsername();
        }
        System.out.println("Type true if you are sure you want to unfollow: ");
        System.out.println(Arrays.toString(onlyFollowersUsernames));
        boolean permission = mysc.nextBoolean();

        if (permission = true) {
            for (int i = 0; i < onlyFollowers.length; i++) {
                instagram.sendRequest(new InstagramUnfollowRequest((Long) onlyFollowers[i]));
            }
        }
        else{
            System.exit(0);
        }
    }
}