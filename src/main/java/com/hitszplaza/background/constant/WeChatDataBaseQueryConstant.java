package com.hitszplaza.background.constant;

public interface WeChatDataBaseQueryConstant {
    String COUNTER = "db.collection('%s').aggregate()\n" +
            "    .match(%s)\n" +
            "    .count('count')\n" +
            "    .end()";

    String USER_POSTER_FIND_ALL = "db.collection('userPoster').aggregate()\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        time: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        likeNum: $.size('$likeList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0,\n" +
            "        userInfo:0,\n" +
            "        likeList:0\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'posterComment',\n" +
            "        let: {\n" +
            "            poster_id: '$_id'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$posterId', '$$poster_id']),\n" +
            "                $.eq(['$valid', true])\n" +
            "            ])))\n" +
            "            .done(),\n" +
            "        as: 'commentList'\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        commentNum: $.size('$commentList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        commentList: 0,\n" +
            "    })\n" +
            "    .end()";

    String USER_POSTER_FIND_BY_OPERATION = "db.collection('userPoster').aggregate()\n" +
            "    .match(%s)\n" +
            "    .sort({\n" +
            "        time: -1\n" +
            "    })\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        likeNum: $.size('$likeList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0,\n" +
            "        userInfo:0,\n" +
            "        likeList:0\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'posterComment',\n" +
            "        let: {\n" +
            "            poster_id: '$_id'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$posterId', '$$poster_id']),\n" +
            "                $.eq(['$valid', true])\n" +
            "            ])))\n" +
            "            .done(),\n" +
            "        as: 'commentList'\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        commentNum: $.size('$commentList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        commentList: 0,\n" +
            "    })\n" +
            "    .end()";

    String USER_POSTER_FIND_BY_ID = "db.collection('userPoster').aggregate()\n" +
            "    .match(_.expr(" +
            "       $.eq(['$_id','%s'])" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        likeNum: $.size('$likeList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0,\n" +
            "        userInfo:0,\n" +
            "        likeList:0\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'posterComment',\n" +
            "        let: {\n" +
            "            poster_id: '$_id'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$posterId', '$$poster_id']),\n" +
            "                $.eq(['$valid', true])\n" +
            "            ])))\n" +
            "            .done(),\n" +
            "        as: 'commentList'\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        commentNum: $.size('$commentList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        commentList: 0,\n" +
            "    })\n" +
            "    .end()";

    String USER_INFO_FIND_BY_OPERATION = "db.collection('userInfo').aggregate()\n" +
            "    .match(%s)\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .end()";

    String FEEDBACK_FIND_ALL = "db.collection('feedBack').aggregate()\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String FEEDBACK_FIND_BY_ID = "db.collection('feedBack').aggregate()\n" +
            "    .match(_.expr(" +
            "       $.eq(['$_id','%s'])" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String FEEDBACK_FIND_BY_CONDITION = "db.collection('feedBack').aggregate()\n" +
            "    .match(%s)\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String GROUPAPPLY_FIND_ALL = "db.collection('groupApply').aggregate()\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String GROUPAPPLY_FIND_BY_ID = "db.collection('groupApply').aggregate()\n" +
            "    .match(_.expr(" +
            "       $.eq(['$_id','%s'])" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String GROUPAPPLY_FIND_BY_CONDITION = "db.collection('groupApply').aggregate()\n" +
            "    .match(%s)\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String GROUP_FIND_ALL = "db.collection('group').aggregate()\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String GROUP_FIND_BY_ID = "db.collection('group').aggregate()\n" +
            "    .match(_.expr(" +
            "       $.eq(['$_id','%s'])" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String GROUP_FIND_BY_CONDITION = "db.collection('group').aggregate()\n" +
            "    .match(%s)\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String REPORT_FIND_ALL = "db.collection('report').aggregate()\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String REPORT_FIND_BY_ID = "db.collection('report').aggregate()\n" +
            "    .match(_.expr(" +
            "       $.eq(['$_id','%s'])" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    String REPORT_FIND_BY_CONDITION = "db.collection('report').aggregate()\n" +
            "    .match(%s)\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        createTime: -1\n" +
            "    })\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    /**
     * 举报总览页面数据的获取
     */
    String REPORT_BATCH_QUERY = "";

    /**
     * 举报中的第一阶段查询：查询举报信息
     */
    String REPORT_STAGE_ONE_REPORT = "db.collection('report').aggregate()\n" +
            "    .match(_.expr(\n" +
            "        $.eq(['$_id','%s'])\n" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$_openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0\n" +
            "    })\n" +
            "    .end()";

    /**
     * 举报中的第二阶段查询：查询举报涉及的帖子
     */
    String REPORT_STAGE_TWO_USER_POSTER = "db.collection('userPoster').aggregate()\n" +
            "    .match(_.expr(\n" +
            "        $.eq(['$_id','%s'])\n" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'userInfo',\n" +
            "        let: {\n" +
            "            user_id: '$openid'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$_id', '$$user_id'])\n" +
            "            ])))\n" +
            "            .project({\n" +
            "                avatarUrl: 1,\n" +
            "                nickName: 1,\n" +
            "            }).done(),\n" +
            "        as: 'author'\n" +
            "    })\n" +
            "    .replaceRoot({\n" +
            "        newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        likeNum: $.size('$likeList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        author: 0,\n" +
            "        userInfo: 0,\n" +
            "        likeList: 0\n" +
            "    })\n" +
            "    .end()";

    /**
     * 举报第三阶段查询：查询相关评论及回复
     */
    String REPORT_STAGE_THREE_CMT_RPL = "db.collection('posterComment').aggregate()\n" +
            "    .skip(%d)\n" +
            "    .limit(%d)\n" +
            "    .sort({\n" +
            "        time: -1\n" +
            "    })\n" +
            "    .match(_.expr(\n" +
            "        $.eq(['$posterId','28ee4e3e60e55c7a287cfa3806e5467f'])\n" +
            "    ))\n" +
            "    .lookup({\n" +
            "        from: 'commentReply',\n" +
            "        let: {\n" +
            "            comment_id: '$_id'\n" +
            "        },\n" +
            "        pipeline: $.pipeline()\n" +
            "            .match(_.expr($.and([\n" +
            "                $.eq(['$commentId', '$$comment_id'])\n" +
            "            ])))\n" +
            "            .lookup({\n" +
            "                from: 'userInfo',\n" +
            "                let: {\n" +
            "                    user_id: '$openid'\n" +
            "                },\n" +
            "                pipeline: $.pipeline()\n" +
            "                    .match(_.expr($.and([\n" +
            "                        $.eq(['$_id', '$$user_id'])\n" +
            "                    ])))\n" +
            "                    .project({\n" +
            "                        avatarUrl: 1,\n" +
            "                        nickName: 1,\n" +
            "                    }).done(),\n" +
            "                as: 'author'\n" +
            "            })\n" +
            "            .replaceRoot({\n" +
            "                newRoot: $.mergeObjects([$.arrayElemAt(['$author', 0]), '$$ROOT'])\n" +
            "            })\n" +
            "            .addFields({\n" +
            "                likeNum: $.size('$likeList')\n" +
            "            })\n" +
            "            .project({\n" +
            "                author: 0,\n" +
            "                openid: 0,\n" +
            "                userInfo: 0,\n" +
            "                commentId: 0,\n" +
            "                posterId: 0\n" +
            "            })\n" +
            "            .done(),\n" +
            "        as: 'replyList'\n" +
            "    })\n" +
            "    .addFields({\n" +
            "        likeNum: $.size('$likeList')\n" +
            "    })\n" +
            "    .project({\n" +
            "        likeList: 0\n" +
            "    })\n" +
            "    .end()";
}


