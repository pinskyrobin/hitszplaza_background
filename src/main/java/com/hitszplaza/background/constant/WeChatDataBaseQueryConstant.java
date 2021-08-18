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
}
