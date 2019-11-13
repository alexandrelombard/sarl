/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2019 the original authors or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.janusproject.services.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.janusproject.services.DependentService;

/**
 * This class enables the Janus kernel to be distributed other a network.
 *
 * @author $Author: srodriguez$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface ExecutorService extends DependentService {

	/** This function simulate the never return from this call.
	 * The {@link EarlyExitException} is thrown.
	 * @since 0.6
	 */
	static void neverReturn() {
		throw new EarlyExitException();
	}

	/**
	 * Replies the JVM executor service used by service.
	 *
	 * @return the JVM executor service.
	 */
	java.util.concurrent.ExecutorService getExecutorService();

	/**
	 * Submit a task to the executor service.
	 *
	 * @param task the task to submit.
	 * @see #submit(Runnable) if you want a future.
	 */
	void execute(Runnable task);

	/**
	 * Submit a single task multiple times to the executor service.
	 *
	 * <p>The same task instance will be submit to the executor service.
	 *
	 * <p>{@code runGroupSize} indicates how many number of times the task will be run on
	 * a single thread.
	 *
	 * <p>This function is equivalent to:
	 * <pre><code>
	 * for(i in [ 1 .. (nbExecutions/runGroupSize) ])
	 * do
	 *     execute({
	 *         for(j in [1..runGroupSize]) {
	 *            task.run
	 *         }
	 *     })
	 * done
	 * </code></pre>
	 *
	 * <p>Caution: if a {@code task} is failing, the exception will be output as an uncaught exception.
	 *
	 * @param task the task to submit.
	 * @param nbExecutions the number of times the task must be run, usually greater than 1.
	 * @param runGroupSize the number of tasks to be run by a single thread.
	 * @return the number of successful runs.
	 * @throws InterruptedException when the function cannot wait for task termination.
	 * @since 0.5
	 */
	int executeMultipleTimesInParallelAndWaitForTermination(Runnable task, int nbExecutions, int runGroupSize) throws InterruptedException;

	/**
	 * Submit a task to the executor service.
	 *
	 * @param task the task to submit.
	 * @return a Future representing the pending execution task.
	 * @see #execute(Runnable) if you don't want a future.
	 */
	Future<?> submit(Runnable task);

	/**
	 * Submit a task to the executor service. The Future's get method will return the given result upon successful completion.
	 *
	 * @param <T> - the type of the value replied by the task.
	 * @param task the task to submit.
	 * @param result result to return after the execution.
	 * @return a Future representing the pending execution task.
	 */
	<T> Future<T> submit(Runnable task, T result);

	/**
	 * Submit a task to the executor service.
	 *
	 * @param <T> - the type of the value replied by the task.
	 * @param task the task to submit.
	 * @return a Future representing the pending execution task.
	 */
	<T> Future<T> submit(Callable<T> task);

	/**
	 * Schedule the given task.
	 *
	 * @param command task to run
	 * @param delay delay for waiting before launching the command
	 * @param unit time unit of the delay
	 * @return a Future representing the pending execution task.
	 */
	ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

	/**
	 * Schedule the given task.
	 *
	 * @param <T> - the type of the value replied by the task.
	 * @param command task to run
	 * @param delay delay for waiting before launching the command
	 * @param unit time unit of the delay
	 * @return a Future representing the pending execution task.
	 */
	<T> ScheduledFuture<T> schedule(Callable<T> command, long delay, TimeUnit unit);

	/**
	 * Creates and executes a periodic action that becomes enabled first after the given initial delay, and subsequently with the
	 * given period; that is executions will commence after initialDelay then initialDelay+period, then initialDelay + 2 * period,
	 * and so on. If any execution of the task encounters an exception, subsequent executions are suppressed. Otherwise, the task
	 * will only terminate via cancellation or termination of the executor. If any execution of this task takes longer than its
	 * period, then subsequent executions may start late, but will not concurrently execute.
	 *
	 * @param command task to run
	 * @param initialDelay the time to delay first execution
	 * @param period the period between successive executions
	 * @param unit è the time unit of the initialDelay and period parameters
	 * @return a Future representing the pending execution task.
	 */
	ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

	/**
	 * Creates and executes a periodic action that becomes enabled first after the given initial delay, and subsequently with the
	 * given delay between the termination of one execution and the commencement of the next. If any execution of the task
	 * encounters an exception, subsequent executions are suppressed. Otherwise, the task will only terminate via cancellation or
	 * termination of the executor.
	 *
	 * @param command the task to execute
	 * @param initialDelay the time to delay first execution
	 * @param delay the delay between the termination of one execution and the start of the next
	 * @param unit the time unit of the initialDelay and delay parameters
	 * @return a ScheduledFuture representing pending completion of the task, and whose get() method will throw an exception upon
	 *         cancellation
	 */
	ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);

	/**
	 * Remove any canceled/terminated tasks from the lists of tasks.
	 */
	void purge();

}
