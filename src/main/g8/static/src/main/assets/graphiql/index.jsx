/* Based on https://github.com/graphql/graphiql/blob/main/packages/graphiql/README.md#using-as-package and
 * https://www.npmjs.com/package/@graphiql/plugin-explorer
 */
import { createGraphiQLFetcher } from '@graphiql/toolkit';
import { GraphiQL } from 'graphiql';
import React from 'react';
import { createRoot } from 'react-dom/client';
import { explorerPlugin } from '@graphiql/plugin-explorer';

import 'graphiql/graphiql.css';
import '@graphiql/plugin-explorer/dist/style.css';

const root = createRoot(document.getElementById('graphiql'));
const fetcher = createGraphiQLFetcher({ url: 'graphql' });
const explorer = explorerPlugin();

root.render(<GraphiQL fetcher={fetcher} plugins={[explorer]} />);
