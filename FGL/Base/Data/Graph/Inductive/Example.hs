module Data.Graph.Inductive.Example
       (genUNodes, genLNodes, labUEdges, noEdges, a, b, c, e, loop, ab,
        abb, dag3, e3, cyc3, g3, g3b, dag4, d1, d3, a', b', c', e', loop',
        ab', abb', dag3', e3', dag4', d1', d3', ucycle, star, ucycleM,
        starM, clr479, clr489, clr486, clr508, clr528, clr595, gr1, kin248,
        vor, clr479', clr489', clr486', clr508', clr528', kin248', vor')
       where
{ import Data.Graph.Inductive;
  import Data.Graph.Inductive.Tree;
  import Data.Graph.Inductive.Monad;
  import Data.Graph.Inductive.Monad.IOArray;
   
  genUNodes :: Int -> [UNode];
  genUNodes n = zip [1 .. n] (repeat ());
   
  genLNodes :: (Enum a) => a -> Int -> [LNode a];
  genLNodes q i = take i (zip [1 ..] [q ..]);
   
  labUEdges :: [Edge] -> [UEdge];
  labUEdges = map (\ (i, j) -> (i, j, ()));
   
  noEdges :: [UEdge];
  noEdges = [];
   
  a, b, c, e, loop, ab, abb, dag3 :: Gr Char ();
   
  e3 :: Gr () String;
   
  cyc3, g3, g3b :: Gr Char String;
   
  dag4 :: Gr Int ();
   
  d1, d3 :: Gr Int Int;
  a = ([], 1, 'a', []) & empty;
  b = mkGraph (zip [1 .. 2] "ab") noEdges;
  c = mkGraph (zip [1 .. 3] "abc") noEdges;
  e = ([((), 1)], 2, 'b', []) & a;
  e3 = mkGraph (genUNodes 2) [(1, 2, "a"), (1, 2, "b"), (1, 2, "a")];
  loop = ([], 1, 'a', [((), 1)]) & empty;
  ab = ([((), 1)], 2, 'b', [((), 1)]) & a;
  abb = mkGraph (zip [1 .. 2] "ab") (labUEdges [(2, 2)]);
  cyc3
    = buildGr
        [([("ca", 3)], 1, 'a', [("ab", 2)]), ([], 2, 'b', [("bc", 3)]),
         ([], 3, 'c', [])];
  dag3 = mkGraph (zip [1 .. 3] "abc") (labUEdges [(1, 3)]);
  dag4
    = mkGraph (genLNodes 1 4)
        (labUEdges [(1, 2), (1, 4), (2, 3), (2, 4), (4, 3)]);
  d1 = mkGraph (genLNodes 1 2) [(1, 2, 1)];
  d3 = mkGraph (genLNodes 1 3) [(1, 2, 1), (1, 3, 4), (2, 3, 2)];
  g3
    = ([("left", 2), ("up", 3)], 1, 'a', [("right", 2)]) &
        (([], 2, 'b', [("down", 3)]) & (([], 3, 'c', []) & empty));
  g3b
    = ([("down", 2)], 3, 'c', [("up", 1)]) &
        (([("right", 1)], 2, 'b', [("left", 1)]) &
           (([], 1, 'a', []) & empty));
   
  a', b', c', e', loop', ab', abb', dag3' :: IO (SGr Char ());
   
  e3' :: IO (SGr () String);
   
  dag4' :: IO (SGr Int ());
   
  d1', d3' :: IO (SGr Int Int);
  a' = mkGraphM [(1, 'a')] noEdges;
  b' = mkGraphM (zip [1 .. 2] "ab") noEdges;
  c' = mkGraphM (zip [1 .. 3] "abc") noEdges;
  e' = mkGraphM (zip [1 .. 2] "ab") [(1, 2, ())];
  e3'
    = mkGraphM (genUNodes 2) [(1, 2, "a"), (1, 2, "b"), (1, 2, "a")];
  loop' = mkGraphM [(1, 'a')] [(1, 1, ())];
  ab' = mkGraphM (zip [1 .. 2] "ab") [(1, 2, ()), (2, 1, ())];
  abb' = mkGraphM (zip [1 .. 2] "ab") (labUEdges [(2, 2)]);
  dag3' = mkGraphM (zip [1 .. 3] "abc") (labUEdges [(1, 3)]);
  dag4'
    = mkGraphM (genLNodes 1 4)
        (labUEdges [(1, 2), (1, 4), (2, 3), (2, 4), (4, 3)]);
  d1' = mkGraphM (genLNodes 1 2) [(1, 2, 1)];
  d3' = mkGraphM (genLNodes 1 3) [(1, 2, 1), (1, 3, 4), (2, 3, 2)];
   
  ucycle :: (Graph gr) => Int -> gr () ();
  ucycle n = mkUGraph vs (map (\ v -> (v, v `mod` n + 1)) vs)
    where { vs = [1 .. n]};
   
  star :: (Graph gr) => Int -> gr () ();
  star n = mkUGraph [1 .. n] (map (\ v -> (1, v)) [2 .. n]);
   
  ucycleM :: (GraphM m gr) => Int -> m (gr () ());
  ucycleM n = mkUGraphM vs (map (\ v -> (v, v `mod` n + 1)) vs)
    where { vs = [1 .. n]};
   
  starM :: (GraphM m gr) => Int -> m (gr () ());
  starM n = mkUGraphM [1 .. n] (map (\ v -> (1, v)) [2 .. n]);
   
  clr479, clr489 :: Gr Char ();
   
  clr486 :: Gr String ();
   
  clr508, clr528 :: Gr Char Int;
   
  clr595, gr1 :: Gr Int Int;
   
  kin248 :: Gr Int ();
   
  vor :: Gr String Int;
  clr479
    = mkGraph (genLNodes 'u' 6)
        (labUEdges
           [(1, 2), (1, 4), (2, 5), (3, 5), (3, 6), (4, 2), (5, 4), (6, 6)]);
  clr486
    = mkGraph
        (zip [1 .. 9]
           ["shorts", "socks", "watch", "pants", "shoes", "shirt", "belt",
            "tie", "jacket"])
        (labUEdges
           [(1, 4), (1, 5), (2, 5), (4, 5), (4, 7), (6, 7), (6, 8), (7, 9),
            (8, 9)]);
  clr489
    = mkGraph (genLNodes 'a' 8)
        (labUEdges
           [(1, 2), (2, 3), (2, 5), (2, 6), (3, 4), (3, 7), (4, 3), (4, 8),
            (5, 1), (5, 6), (6, 7), (7, 6), (7, 8), (8, 8)]);
  clr508
    = mkGraph (genLNodes 'a' 9)
        [(1, 2, 4), (1, 8, 8), (2, 3, 8), (2, 8, 11), (3, 4, 7), (3, 6, 4),
         (3, 9, 2), (4, 5, 9), (4, 6, 14), (5, 6, 10), (6, 7, 2), (7, 8, 1),
         (7, 9, 6), (8, 9, 7)];
  clr528
    = mkGraph [(1, 's'), (2, 'u'), (3, 'v'), (4, 'x'), (5, 'y')]
        [(1, 2, 10), (1, 4, 5), (2, 3, 1), (2, 4, 2), (3, 5, 4), (4, 2, 3),
         (4, 3, 9), (4, 5, 2), (5, 1, 7), (5, 3, 6)];
  clr595
    = mkGraph (zip [1 .. 6] [1 .. 6])
        [(1, 2, 16), (1, 3, 13), (2, 3, 10), (2, 4, 12), (3, 2, 4),
         (3, 5, 14), (4, 3, 9), (4, 6, 20), (5, 4, 7), (5, 6, 4)];
  gr1
    = mkGraph (zip [1 .. 10] [1 .. 10])
        [(1, 2, 12), (1, 3, 1), (1, 4, 2), (2, 3, 1), (2, 5, 7), (2, 6, 5),
         (3, 6, 1), (3, 7, 7), (4, 3, 3), (4, 6, 2), (4, 7, 5), (5, 3, 2),
         (5, 6, 3), (5, 8, 3), (6, 7, 2), (6, 8, 3), (6, 9, 1), (7, 9, 9),
         (8, 9, 1), (8, 10, 4), (9, 10, 11)];
  kin248
    = mkGraph (genLNodes 1 10)
        (labUEdges
           [(1, 2), (1, 4), (1, 7), (2, 4), (2, 5), (3, 4), (3, 10), (4, 5),
            (4, 8), (5, 2), (5, 3), (6, 7), (7, 6), (7, 8), (8, 10), (9, 9),
            (9, 10), (10, 8), (10, 9)]);
  vor
    = mkGraph (zip [1 .. 8] ["A", "B", "C", "H1", "H2", "D", "E", "F"])
        [(1, 4, 3), (2, 3, 3), (2, 4, 3), (4, 2, 4), (4, 6, 2), (5, 2, 5),
         (5, 3, 6), (5, 7, 5), (5, 8, 6), (6, 5, 3), (6, 7, 2), (7, 8, 3),
         (8, 7, 3)];
   
  clr479', clr489' :: IO (SGr Char ());
   
  clr486' :: IO (SGr String ());
   
  clr508', clr528' :: IO (SGr Char Int);
   
  kin248' :: IO (SGr Int ());
   
  vor' :: IO (SGr String Int);
  clr479'
    = mkGraphM (genLNodes 'u' 6)
        (labUEdges
           [(1, 2), (1, 4), (2, 5), (3, 5), (3, 6), (4, 2), (5, 4), (6, 6)]);
  clr486'
    = mkGraphM
        (zip [1 .. 9]
           ["shorts", "socks", "watch", "pants", "shoes", "shirt", "belt",
            "tie", "jacket"])
        (labUEdges
           [(1, 4), (1, 5), (2, 5), (4, 5), (4, 7), (6, 7), (6, 8), (7, 9),
            (8, 9)]);
  clr489'
    = mkGraphM (genLNodes 'a' 8)
        (labUEdges
           [(1, 2), (2, 3), (2, 5), (2, 6), (3, 4), (3, 7), (4, 3), (4, 8),
            (5, 1), (5, 6), (6, 7), (7, 6), (7, 8), (8, 8)]);
  clr508'
    = mkGraphM (genLNodes 'a' 9)
        [(1, 2, 4), (1, 8, 8), (2, 3, 8), (2, 8, 11), (3, 4, 7), (3, 6, 4),
         (3, 9, 2), (4, 5, 9), (4, 6, 14), (5, 6, 10), (6, 7, 2), (7, 8, 1),
         (7, 9, 6), (8, 9, 7)];
  clr528'
    = mkGraphM [(1, 's'), (2, 'u'), (3, 'v'), (4, 'x'), (5, 'y')]
        [(1, 2, 10), (1, 4, 5), (2, 3, 1), (2, 4, 2), (3, 5, 4), (4, 2, 3),
         (4, 3, 9), (4, 5, 2), (5, 1, 7), (5, 3, 6)];
  kin248'
    = mkGraphM (genLNodes 1 10)
        (labUEdges
           [(1, 2), (1, 4), (1, 7), (2, 4), (2, 5), (3, 4), (3, 10), (4, 5),
            (4, 8), (5, 2), (5, 3), (6, 7), (7, 6), (7, 8), (8, 10), (9, 9),
            (9, 10), (10, 8), (10, 9)]);
  vor'
    = mkGraphM
        (zip [1 .. 8] ["A", "B", "C", "H1", "H2", "D", "E", "F"])
        [(1, 4, 3), (2, 3, 3), (2, 4, 3), (4, 2, 4), (4, 6, 2), (5, 2, 5),
         (5, 3, 6), (5, 7, 5), (5, 8, 6), (6, 5, 3), (6, 7, 2), (7, 8, 3),
         (8, 7, 3)]}
