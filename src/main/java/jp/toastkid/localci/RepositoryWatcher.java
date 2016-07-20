package jp.toastkid.localci;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class RepositoryWatcher {

    private final Repository repo;

    public RepositoryWatcher(final String path) throws IOException {
        this.repo = new FileRepositoryBuilder()
                .setGitDir(new File(path))
                .build();
    }

    public String findLatestRevisionHash(final String branchName) throws IOException {
        return this.repo.findRef(branchName).getObjectId().name();
    }

    public static void main(final String[] args) throws IOException {
        final Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File("../Yobidashi/.git"))
                .build();
        // 参照を取得する
        final Ref master = repo.findRef("master");

        // 参照の指すオブジェクトを取得する
        final ObjectId masterTip = master.getObjectId();

        System.out.println(masterTip.name());

    }
}
