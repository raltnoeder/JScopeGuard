package scopeguard;

/**
 * Implements a ScopeGuard for use in a try-with-resources scope
 *
 * A ScopeGuard instance allows adding actions that will be executed
 * either
 *     - unconditionally
 *     - upon successful exit
 *     - upon unsuccessful exit
 * from the scope managed by that ScopeGuard instance.
 *
 * Actions added for unconditional execution are always executed when
 * the scope is left.
 *
 * Unless the execution of the scope was explicitly declared successful
 * by the time the scope is left, actions for unsuccessful exit from
 * the scope are executed.
 *
 * If the execution of the scope was explicitly declared successful by
 * the time the scope is left, actions for successful exit from the
 * scope are executed.
 *
 * @author Robert Altnoeder &lt;r.altnoeder@gmx.net&gt;
 */
public class ScopeGuard implements AutoCloseable
{
    private Entry successChain  = null;
    private Entry failChain     = null;

    private boolean successFlag = false;

    /**
     * Adds an action to be executed unconditionally upon leaving
     * the scope managed by this ScopeGuard instance.
     *
     * The action will be executed unconditionally upon leaving
     * the scope managed by this ScopeGuard instance.
     *
     * It is strongly recommended to ensure that the action does
     * not throw any exceptions.
     * Exceptions thrown by the action during its execution are ignored.
     *
     * @param action The action to execute upon leaving the scope
     * @return This ScopeGuard instance
     */
    public ScopeGuard onExit(final Runnable action)
    {
        successChain = new Entry(action, successChain);
        failChain = new Entry(action, failChain);

        return this;
    }

    /**
     * Adds an action to be executed upon successful exit from the
     * scope managed by this ScopeGuard instance.
     *
     * The action will be executed ONLY if this ScopeGuard instance
     * was set to treat the execution of the scope as successful
     * by the time the scope is left.
     *
     * It is strongly recommended to ensure that the action does
     * not throw any exceptions.
     * Exceptions thrown by the action during its execution are ignored.
     *
     * @param action The action to execute upon leaving the scope
     * @return This ScopeGuard instance
     */
    public ScopeGuard onSuccess(final Runnable action)
    {
        successChain = new Entry(action, successChain);

        return this;
    }

    /**
     * Adds an action to be executed upon unsuccessful exit from the
     * scope managed by this ScopeGuard instance.
     *
     * The action will be executed UNLESS this ScopeGuard instance
     * was set to treat the execution of the scope as successful
     * by the time the scope is left.
     *
     * It is strongly recommended to ensure that the action does
     * not throw any exceptions.
     * Exceptions thrown by the action during its execution are ignored.
     *
     * @param action The action to execute upon leaving the scope
     * @return This ScopeGuard instance
     */
    public ScopeGuard onFailure(final Runnable action)
    {
        failChain = new Entry(action, failChain);

        return this;
    }

    /**
     * Declares the execution of the scope managed by this
     * ScopeGuard instance successful.
     *
     * Upon leaving the scope, only actions added for unconditional
     * execution and actions for successful exit from the scope
     * will be executed.
     *
     * @return This ScopeGuard instance
     */
    public ScopeGuard declareSuccessful()
    {
        successFlag = true;
        failChain = null;
        
        return this;
    }

    @Override
    public void close()
    {
        Entry chainHead;
        if (successFlag)
        {
            chainHead = successChain;
        }
        else
        {
            successChain = null;
            chainHead = failChain;
        }

        for (Entry curEntry = chainHead; curEntry != null; curEntry = curEntry.next)
        {
            try
            {
                curEntry.entryAction.run();
            }
            catch (Exception ignored)
            {
            }
        }
    }

    private static class Entry
    {
        private final Runnable entryAction;
        private final Entry next;

        Entry(final Runnable actionRef, final Entry nextRef)
        {
            entryAction = actionRef;
            next = nextRef;
        }
    }
}
