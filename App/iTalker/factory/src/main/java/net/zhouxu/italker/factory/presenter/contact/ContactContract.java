package net.zhouxu.italker.factory.presenter.contact;

import net.zhouxu.italker.factory.model.db.User;
import net.zhouxu.italker.factory.presenter.BaseContract;

/**
 * Created by zx on 2018/5/31.
 */

public interface ContactContract {

    interface Presenter extends BaseContract.Presenter{

    }

    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }
}
