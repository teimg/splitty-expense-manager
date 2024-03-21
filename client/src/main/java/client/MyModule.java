/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.ModelView.StartScreenMv;
import client.language.Translator;
import client.scenes.*;
import client.utils.EventCommunicator;
import client.utils.IEventCommunicator;
import client.utils.RecentEventTracker;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddQuoteCtrl.class).in(Scopes.SINGLETON);
        binder.bind(QuoteOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddEditExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OpenDebtsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StatisticsScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ContactInfoCtrl.class).in(Scopes.SINGLETON);

        binder.bind(MenuBarCtrl.class).in(Scopes.SINGLETON);
        // Ensures all config reading/writing goes through a central ClientConfiguration class.
//        binder.bind(ClientConfiguration.class).in(Scopes.SINGLETON);
        binder.bind(Translator.class).in(Scopes.SINGLETON);

        binder.bind(RecentEventTracker.class).in(Scopes.SINGLETON);

        binder.bind(AdminLogInCtrl.class).in(Scopes.SINGLETON);

        binder.bind(IEventCommunicator.class).to(EventCommunicator.class).in(Scopes.SINGLETON);
        try {
            binder.bind(StartScreenMv.class).toConstructor(
                StartScreenMv.class.getConstructor(
                    IEventCommunicator.class, RecentEventTracker.class))
                .in(Scopes.SINGLETON);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        binder.bind(AdminScreenCtrl.class).in(Scopes.SINGLETON);
    }
}